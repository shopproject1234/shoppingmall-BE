package com.sangwook.shoppingmall.concurrent;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.assertj.core.util.introspection.CaseFormatUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CleanUp implements InitializingBean {

    @Autowired
    EntityManager em;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
        tableNames = em.getMetamodel().getEntities().stream()
                .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormatUtils.toCamelCase(e.getName())).toList();
    }

    @Transactional
    public void execute() {
        em.flush();
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            if (tableName.equals("user")) {
                tableName = "users";
            }
            if (tableName.equals("itemImage")) {
                tableName = "item_image";
            }
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            em.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

    }
}
