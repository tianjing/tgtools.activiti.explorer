package tgtools.activiti.explorer.config;



import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import tgtools.activiti.explorer.gateway.FlowController;
import tgtools.activiti.explorer.service.FlowService;
import tgtools.activiti.explorer.service.FlowTreeViewService;
import tgtools.activiti.explorer.service.FlowViewService;
import tgtools.activiti.impl.ProcessEngineConfigurationImpl;
import tgtools.activiti.modeler.gateway.ModelController;
import tgtools.activiti.modeler.gateway.ResourceController;

import javax.sql.DataSource;

/**
 * 建表 ：建表sql：activiti-engine.jar ->org/activiti/db/create
 * 管理页面：
 * http://ip:port/${context-path}/activiti/explorer/manage/resource/model.html
 * http://ip:port/${context-path}/activiti/explorer/manage/resource/flow.html
 * 模型编辑页面：
 * http://ip:port/${context-path}/activiti/resource/modeler.html?modelId=57501
 *
 */
@Configuration
@MapperScan(basePackages = {"tgtools.activiti.explorer.dao"}, sqlSessionFactoryRef = "activitiExplorerSqlSessionFactory" )
public class ActivitiConfig {
    /**
     * 流程配置，与spring整合采用SpringProcessEngineConfiguration这个实现
     * @param dataSource
     * @param transactionManager
     * @return
     */
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager){
       // SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        ProcessEngineConfigurationImpl processEngineConfiguration =new ProcessEngineConfigurationImpl();
        //设置数据源
        processEngineConfiguration.setDataSource(dataSource);
        //自动更新表
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        //达梦6
        //processEngineConfiguration.setDatabaseType("dm6");
        //达梦7
        processEngineConfiguration.setDatabaseType("dm");
        //使用guid 主键
        processEngineConfiguration.setIdGenerator(new org.activiti.engine.impl.persistence.StrongUuidGenerator());
        //支持事物
        processEngineConfiguration.setTransactionManager(transactionManager);

        //流程图字体
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");

        return processEngineConfiguration;
    }

    /**
     * 流程引擎，与spring整合使用factoryBean
     * @param processEngineConfiguration
     * @return
     */
    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfiguration processEngineConfiguration){
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        return processEngineFactoryBean;
    }

    //八大接口
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine){
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine){
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine){
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine){
        return processEngine.getHistoryService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine){
        return processEngine.getFormService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine){
        return processEngine.getIdentityService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine){
        return processEngine.getManagementService();
    }

    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine){
        return processEngine.getDynamicBpmnService();
    }

    /**
     * 开启 activiti 模型功能编辑器 rest功能 开关
     * @param processEngine
     * @return
     */
    @Bean
    public ModelController modelController(ProcessEngine processEngine){
        return new ModelController();
    }


    /**
     * 开启 activiti 模型编辑器页面功能 开关
     * 访问路径 如： http://ip:port/${context-path}/activiti/resource/modeler.html?modelId=57501
     * @param processEngine
     * @return
     */
    @Bean
    public ResourceController resourceController(ProcessEngine processEngine){
        return new ResourceController();
    }
    /**
     * 流程管理 REST 接口 开关
     * @return
     */
    @Bean
    public FlowController flowController(){
        return new FlowController();
    }


    /**
     * 开启 activiti 管理页面 开关
     * 访问路径 如：
     * http://ip:port/${context-path}/activiti/explorer/manage/resource/model.html
     * http://ip:port/${context-path}/activiti/explorer/manage/resource/flow.html
     * @return
     */
    @Bean
    public tgtools.activiti.explorer.gateway.ResourceController flowResourceController(){
        return new tgtools.activiti.explorer.gateway.ResourceController();
    }


    /**
     *  业务功能：常用流程流转功能 API 封装
     * @return
     */
    @Bean
    public FlowService flowService(){
    return new FlowService();
    }

    /**
     * 流程查询服务
     * @return
     */
    @Bean
    public FlowViewService flowViewService(){
        return new FlowViewService();
    }

    public FlowTreeViewService flowTreeViewService(){
        return new FlowTreeViewService();
    }


    @Bean
    public SqlSessionFactory activitiExplorerSqlSessionFactory(@Qualifier("dataSource")DataSource dataSource) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
//        org.apache.ibatis.session.Configuration config =new org.apache.ibatis.session.Configuration();
//        config.setMapUnderscoreToCamelCase(true);
//        factoryBean.setConfiguration(config);
        return factoryBean.getObject();

    }

    @Bean
    public SqlSessionTemplate activitiExplorerSqlSessionTemplate(@Qualifier("activitiExplorerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }
}
