package com.rt.common.realm;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by huy  on 2022/12/21.
 */
public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
//        if (oldVersion == 1) {
//            RealmObjectSchema calenderSchema = schema.create("CalenderEventIdBean")
//                    .addField("eventId", Long.class, FieldAttribute.PRIMARY_KEY).setRequired("eventId", true);
//            oldVersion++;
//        }
    }
}
