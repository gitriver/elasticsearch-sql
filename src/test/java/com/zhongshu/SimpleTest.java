package com.zhongshu;

import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_CONNECTIONPROPERTIES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;

/**
* @Description: TODO(用一句话描述该文件做什么)
* @author jzz
* @date 2019年12月26日
* @version V1.0
*/
public class SimpleTest {
    public static void main(String[] args) throws Exception {
        testJDBC();
    }

    public static void testJDBC() throws Exception {
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://172.25.21.17:9300/" + "resource_geo");
        properties.put(PROP_CONNECTIONPROPERTIES, "client.transport.ignore_cluster_name=true");

        DruidDataSource dds = (DruidDataSource)ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        Connection connection = dds.getConnection();

        PreparedStatement ps = connection.prepareStatement("SELECT  data_src,data_cate,name from  " + "resource_geo limit 10");
        ResultSet resultSet = ps.executeQuery();
        
        ResultSetMetaData metaData = resultSet.getMetaData();
        System.out.println(metaData.getColumnCount());
        for (int i = 1; i < metaData.getColumnCount(); i++) {
            System.out.println(String.format("name:%s,type:%s", metaData.getColumnName(i), metaData.getColumnType(i)));
        }
        
        List<String> result = new ArrayList<String>();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("data_src") + "," + resultSet.getInt("name") + ","
                + resultSet.getString("data_cate"));
        }
        ps.close();
        connection.close();
        dds.close();
    }
}
