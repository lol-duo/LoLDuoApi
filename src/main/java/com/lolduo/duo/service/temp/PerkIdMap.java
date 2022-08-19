package com.lolduo.duo.service.temp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
public class PerkIdMap {
    Map<Long,List<PerkCheck>> mainPerkMap = new HashMap<>();
    Map<Long,List<PerkCheck>> secondaryPerkMap = new HashMap<>();

    List<PerkCheck> subSubPerkList = new ArrayList<>();
    public PerkIdMap() {

        //8000 정밀
        List<PerkCheck> mainPerkCheckList =new ArrayList<>();
        List<PerkCheck> subPerkCheckList =new ArrayList<>();

        List<Long> perkIdList = new ArrayList<>();
        perkIdList.add(8005L);
        perkIdList.add(8008L);
        perkIdList.add(8021L);
        perkIdList.add(8010L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(9101L);
        perkIdList.add(9111L);
        perkIdList.add(8009L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(9104L);
        perkIdList.add(9105L);
        perkIdList.add(9103L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8014L);
        perkIdList.add(8017L);
        perkIdList.add(8299L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));
        mainPerkMap.put(8000L,mainPerkCheckList);
        secondaryPerkMap.put(8000L,subPerkCheckList);

        //지배
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();
        perkIdList = new ArrayList<>();
        perkIdList.add(8112L);
        perkIdList.add(8124L);
        perkIdList.add(8128L);
        perkIdList.add(9923L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8126L);
        perkIdList.add(8139L);
        perkIdList.add(8143L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8136L);
        perkIdList.add(8120L);
        perkIdList.add(8138L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8135L);
        perkIdList.add(8134L);
        perkIdList.add(8105L);
        perkIdList.add(8106L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));
        mainPerkMap.put(8100L,mainPerkCheckList);
        secondaryPerkMap.put(8100L,subPerkCheckList);


        //결의
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();
        perkIdList = new ArrayList<>();
        perkIdList.add(8437L);
        perkIdList.add(8439L);
        perkIdList.add(8465L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8446L);
        perkIdList.add(8463L);
        perkIdList.add(8401L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8429L);
        perkIdList.add(8444L);
        perkIdList.add(8473L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8451L);
        perkIdList.add(8453L);
        perkIdList.add(8242L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));
        mainPerkMap.put(8400L,mainPerkCheckList);
        secondaryPerkMap.put(8400L,subPerkCheckList);

        //영감
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();
        perkIdList = new ArrayList<>();
        perkIdList.add(8351L);
        perkIdList.add(8360L);
        perkIdList.add(8369L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8306L);
        perkIdList.add(8304L);
        perkIdList.add(8313L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8321L);
        perkIdList.add(8316L);
        perkIdList.add(8345L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8347L);
        perkIdList.add(8410L);
        perkIdList.add(8352L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));
        mainPerkMap.put(8300L,mainPerkCheckList);
        secondaryPerkMap.put(8300L,subPerkCheckList);


        //마법
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();
        perkIdList = new ArrayList<>();
        perkIdList.add(8214L);
        perkIdList.add(8229L);
        perkIdList.add(8230L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8224L);
        perkIdList.add(8226L);
        perkIdList.add(8275L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8210L);
        perkIdList.add(8234L);
        perkIdList.add(8233L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(8237L);
        perkIdList.add(8232L);
        perkIdList.add(8236L);
        mainPerkCheckList.add(new PerkCheck(perkIdList,false));
        subPerkCheckList.add(new PerkCheck(perkIdList,false));
        mainPerkMap.put(8200L,mainPerkCheckList);
        secondaryPerkMap.put(8200L,subPerkCheckList);

        //스탯 룬
        List<PerkCheck> subSubCheckList = new ArrayList<>();
        perkIdList = new ArrayList<>();
        perkIdList.add(5008L);
        perkIdList.add(5005L);
        perkIdList.add(5007L);
        subSubCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(5008L);
        perkIdList.add(5002L);
        perkIdList.add(5003L);
        subSubCheckList.add(new PerkCheck(perkIdList,false));

        perkIdList = new ArrayList<>();
        perkIdList.add(5001L);
        perkIdList.add(5002L);
        perkIdList.add(5003L);
        subSubCheckList.add(new PerkCheck(perkIdList,false));
        subSubPerkList = subPerkCheckList;
    }

    public class PerkCheck{
        List<String> perkList;
        List<Long> perkIdList;
        Boolean isActive;

        public PerkCheck(List<Long> perkIdList, Boolean isActive) {
            this.perkIdList = perkIdList;
            this.isActive = isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }
        public List<String> getPerkList() {
            return perkList;
        }

        public Boolean getActive() {
            return isActive;
        }
    }
}
