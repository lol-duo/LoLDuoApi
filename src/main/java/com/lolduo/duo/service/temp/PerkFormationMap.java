package com.lolduo.duo.service.temp;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
public class PerkFormationMap {
    Map<Long, List<PerkCheck>> mainPerkMap = new HashMap<>();
    Map<Long, List<PerkCheck>> secondaryPerkMap = new HashMap<>();
    List<PerkCheck> statModList = new ArrayList<>();

    public PerkFormationMap() {
        initPerkMapAndList();
    }

    public void initPerkMapAndList() {
        //8000 정밀
        List<PerkCheck> mainPerkCheckList = new ArrayList<>();
        List<PerkCheck> subPerkCheckList = new ArrayList<>();

        List<Long> perkIdList = new ArrayList<>();
        ArrayList<String> perkUrlList = new ArrayList<>();
        perkIdList.add(8005L);
        perkIdList.add(8008L);
        perkIdList.add(8021L);
        perkIdList.add(8010L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/PressTheAttack/PressTheAttack.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/LethalTempo/LethalTempoTemp.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/FleetFootwork/FleetFootwork.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/Conqueror/Conqueror.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(9101L);
        perkIdList.add(9111L);
        perkIdList.add(8009L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/Overheal.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/Triumph.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/PresenceOfMind/PresenceOfMind.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(9104L);
        perkIdList.add(9105L);
        perkIdList.add(9103L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/LegendAlacrity/LegendAlacrity.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/LegendTenacity/LegendTenacity.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/LegendBloodline/LegendBloodline.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8014L);
        perkIdList.add(8017L);
        perkIdList.add(8299L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/CoupDeGrace/CoupDeGrace.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/CutDown/CutDown.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/LastStand/LastStand.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        mainPerkMap.put(8000L,mainPerkCheckList);
        secondaryPerkMap.put(8000L,subPerkCheckList);

        //지배
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8112L);
        perkIdList.add(8124L);
        perkIdList.add(8128L);
        perkIdList.add(9923L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/Electrocute/Electrocute.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/Predator/Predator.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/DarkHarvest/DarkHarvest.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/HailOfBlades/HailOfBlades.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8126L);
        perkIdList.add(8139L);
        perkIdList.add(8143L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/CheapShot/CheapShot.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/TasteOfBlood/GreenTerror_TasteOfBlood.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/SuddenImpact/SuddenImpact.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8136L);
        perkIdList.add(8120L);
        perkIdList.add(8138L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/ZombieWard/ZombieWard.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/GhostPoro/GhostPoro.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/EyeballCollection/EyeballCollection.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8135L);
        perkIdList.add(8134L);
        perkIdList.add(8105L);
        perkIdList.add(8106L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/TreasureHunter/TreasureHunter.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/IngeniousHunter/IngeniousHunter.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/RelentlessHunter/RelentlessHunter.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/UltimateHunter/UltimateHunter.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        mainPerkMap.put(8100L,mainPerkCheckList);
        secondaryPerkMap.put(8100L,subPerkCheckList);

        //결의
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8437L);
        perkIdList.add(8439L);
        perkIdList.add(8465L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/GraspOfTheUndying/GraspOfTheUndying.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/VeteranAftershock/VeteranAftershock.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Guardian/Guardian.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8446L);
        perkIdList.add(8463L);
        perkIdList.add(8401L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Demolish/Demolish.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/FontOfLife/FontOfLife.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/MirrorShell/MirrorShell.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8429L);
        perkIdList.add(8444L);
        perkIdList.add(8473L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Conditioning/Conditioning.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/SecondWind/SecondWind.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/BonePlating/BonePlating.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8451L);
        perkIdList.add(8453L);
        perkIdList.add(8242L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Overgrowth/Overgrowth.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Revitalize/Revitalize.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Unflinching/Unflinching.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        mainPerkMap.put(8400L,mainPerkCheckList);
        secondaryPerkMap.put(8400L,subPerkCheckList);

        //영감
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8351L);
        perkIdList.add(8360L);
        perkIdList.add(8369L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/GlacialAugment/GlacialAugment.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/UnsealedSpellbook/UnsealedSpellbook.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/FirstStrike/FirstStrike.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8306L);
        perkIdList.add(8304L);
        perkIdList.add(8313L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/HextechFlashtraption/HextechFlashtraption.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/MagicalFootwear/MagicalFootwear.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/PerfectTiming/PerfectTiming.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8321L);
        perkIdList.add(8316L);
        perkIdList.add(8345L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/FuturesMarket/FuturesMarket.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/MinionDematerializer/MinionDematerializer.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/BiscuitDelivery/BiscuitDelivery.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8347L);
        perkIdList.add(8410L);
        perkIdList.add(8352L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/CosmicInsight/CosmicInsight.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/ApproachVelocity/ApproachVelocity.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/TimeWarpTonic/TimeWarpTonic.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        mainPerkMap.put(8300L,mainPerkCheckList);
        secondaryPerkMap.put(8300L,subPerkCheckList);

        //마법
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8214L);
        perkIdList.add(8229L);
        perkIdList.add(8230L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/SummonAery/SummonAery.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/ArcaneComet/ArcaneComet.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/PhaseRush/PhaseRush.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8224L);
        perkIdList.add(8226L);
        perkIdList.add(8275L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/NullifyingOrb/Pokeshield.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/ManaflowBand/ManaflowBand.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/NimbusCloak/6361.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8210L);
        perkIdList.add(8234L);
        perkIdList.add(8233L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Transcendence/Transcendence.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Celerity/CelerityTemp.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/AbsoluteFocus/AbsoluteFocus.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(8237L);
        perkIdList.add(8232L);
        perkIdList.add(8236L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Scorch/Scorch.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Waterwalking/Waterwalking.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/GatheringStorm/GatheringStorm.png");
        mainPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        subPerkCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        mainPerkMap.put(8200L,mainPerkCheckList);
        secondaryPerkMap.put(8200L,subPerkCheckList);

        //스탯 룬
        List<PerkCheck> statModCheckList = new ArrayList<>();

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(5008L);
        perkIdList.add(5005L);
        perkIdList.add(5007L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsAdaptiveForceIcon.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsAttackSpeedIcon.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsCDRScalingIcon.png");
        statModCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(5008L);
        perkIdList.add(5002L);
        perkIdList.add(5003L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsAdaptiveForceIcon.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsArmorIcon.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsMagicResIcon.MagicResist_Fix.png");
        statModCheckList.add(new PerkCheck(perkIdList, perkUrlList));

        perkIdList = new ArrayList<>();
        perkUrlList = new ArrayList<>();
        perkIdList.add(5001L);
        perkIdList.add(5002L);
        perkIdList.add(5003L);
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsHealthScalingIcon.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsArmorIcon.png");
        perkUrlList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsMagicResIcon.MagicResist_Fix.png");
        statModCheckList.add(new PerkCheck(perkIdList, perkUrlList));
        statModList = statModCheckList;
    }

    public class PerkCheck{
        ArrayList<String> perkUrlList;
        List<Long> perkIdList;

        public PerkCheck(List<Long> perkIdList, ArrayList<String> perkUrlList) {
            this.perkIdList = perkIdList;
            this.perkUrlList = perkUrlList;
        }

        public List<String> getPerkUrlList() {
            return perkUrlList;
        }

        public void disableInactivePerks(List<Long> activePerkList) {
            int index = 0;
            for (Long perkId : perkIdList) {
                if (!activePerkList.contains(perkId))
                    perkUrlList.set(index, perkUrlList.get(index) + "_disabled.png");
                index++;
            }
        }

        public void disablePerksExcept(Long exceptPerkId) {
            int index = 0;
            log.info("disablePerksExcept - exceptPerkId : {}, perkCheck's perkIdList : {}", exceptPerkId, perkIdList);
            for (Long perkId : perkIdList) {
                if (!perkId.equals(exceptPerkId))
                    perkUrlList.set(index, perkUrlList.get(index) + "_disabled.png");
                index++;
            }
        }
    }
}
