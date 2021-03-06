package com.fqy.crm;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.crm.dao.paper.PaperDao;
import com.fqy.crm.entity.building.Bed;
import com.fqy.crm.entity.building.Building;
import com.fqy.crm.entity.building.Room;
import com.fqy.crm.entity.entityutils.ResponseMessage;
import com.fqy.crm.entity.paper.Paper;
import com.fqy.crm.entity.paper.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CrmApplicationTests {

    @Resource
    private PaperDao paperDao;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void test() {

        System.out.println(paperDao.selectById(1));
    }

    @Test
    void paperTest() {
        ObjectMapper om = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8123/paper/all";
        ResponseMessage response = restTemplate.getForObject(url, ResponseMessage.class);
        List<Paper> papers = new ArrayList<>();
        if (response.getCode() == 200) {
            try {
                List paperResult = (List) response.getData();
                for (Object paperObj : paperResult) {

                    String paperJsonStr = om.writeValueAsString(paperObj);
                    System.out.println(paperJsonStr);
                    Paper paper = om.readValue(paperJsonStr, Paper.class);

                    List<Question> questionResult = paper.getQuestionList();
                    List<Question> questions = new ArrayList<>(questionResult.size());
                    for(Object questionObj : questionResult){
                        String questionJsonStr = om.writeValueAsString(questionObj);
                        System.out.println(questionJsonStr);
                        Question question = om.readValue(questionJsonStr, Question.class);

                        questions.add(question);
                    }
                    paper.setQuestionList(questions);
                    papers.add(paper);
                }
                System.out.println(papers);
            } catch (JsonProcessingException e) {
                e.printStackTrace();

            }
        }
    }

    @Test
    void updatePaperTest(){

    }

//    @Test
//    void contextLoads() {
//        ObjectMapper om = new ObjectMapper();
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8123/building/all";
//        ResponseMessage response = restTemplate.getForObject(url, ResponseMessage.class);
//        List<Building> buildings = new ArrayList<>();
//        if (response.getCode() == 200) {
//            try {
//                List buildingResult = (List) response.getData();
//
//                for (Object buildingObj : buildingResult) {
//
//                    String buildingJsonStr = om.writeValueAsString(buildingObj);
//                    System.out.println(buildingJsonStr);
//                    Building building = om.readValue(buildingJsonStr, Building.class);
//
//                    List<Room> roomResult = building.getRoomList();
//                    List<Room> rooms = new ArrayList<>(roomResult.size());
//                    for (Object roomObj : roomResult) {
//                        String roomJsonStr = om.writeValueAsString(roomObj);
//                        System.out.println(roomJsonStr);
//                        Room room = om.readValue(roomJsonStr, Room.class);
//
//                        List<Bed> bedResult = room.getBedList();
//                        List<Bed> beds = new ArrayList<>(bedResult.size());
//                        for (Object bedObj : bedResult) {
//                            String bedJsonStr = om.writeValueAsString(bedObj);
//                            System.out.println(bedJsonStr);
//                            Bed bed = om.readValue(bedJsonStr, Bed.class);
//
//                            beds.add(bed);
//                        }
//
//                        room.setBedList(beds);
//                        rooms.add(room);
//                    }
//
//                    building.setRoomList(rooms);
//                    buildings.add(building);
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//
//        }
//        System.out.println(buildings);
//    }

    @Test
    void contextLoads() {
        DataSource dataSourceMysql = applicationContext.getBean(DataSource.class);

        // ??????????????????
        EngineConfig engineConfig = EngineConfig.builder()
                // ???????????????????????????mac???????????????????????????????????????????????????
                .fileOutputDir("D:/database_table_doc/")
                // ????????????
                .openOutputDir(false)
                // ????????????
                .fileType(EngineFileType.WORD)
                // ??????????????????
                .produceType(EngineTemplateType.freemarker).build();

        // ??????????????????????????????????????????????????????????????????????????????
        Configuration config = Configuration.builder()
                .version("1.0.3")
                .description("????????????????????????")
                .dataSource(dataSourceMysql)
                .engineConfig(engineConfig)
                .produceConfig(getProcessConfig())
                .build();

        // ????????????
        new DocumentationExecute(config).execute();
    }


    /**
     * ????????????????????????+ ????????????????????????
     * @return ???????????????
     */
    public static ProcessConfig getProcessConfig(){
        // ????????????
        List<String> ignoreTableName = Arrays.asList("aa","test_group");
        // ???????????????????????????a?????????????????????
        List<String> ignorePrefix = Arrays.asList("a","t");
        // ???????????????
        List<String> ignoreSuffix = Arrays.asList("_test","czb_");

//        return ProcessConfig.builder()
//                //???????????????????????????
//                .designatedTableName(new ArrayList<>())
//                //?????????????????????
//                .designatedTablePrefix(new ArrayList<>())
//                //?????????????????????
//                .designatedTableSuffix(new ArrayList<>())
//                //????????????
//                .ignoreTableName(ignoreTableName)
//                //???????????????
//                .ignoreTablePrefix(ignorePrefix)
//                //???????????????
//                .ignoreTableSuffix(ignoreSuffix).build();
        return ProcessConfig.builder().build();
    }
}
