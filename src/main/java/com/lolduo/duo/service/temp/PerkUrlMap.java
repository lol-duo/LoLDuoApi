package com.lolduo.duo.service.temp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
public class PerkUrlMap {
    Map<Long,List<PerkCheck>> mainPerkMap = new HashMap<>();
    Map<Long,List<PerkCheck>> secondaryPerkMap = new HashMap<>();

    List<PerkCheck> subSubPerkList = new ArrayList<>();
    public PerkUrlMap() {

        //8000 정밀

        List<PerkCheck> mainPerkCheckList =new ArrayList<>();
        List<PerkCheck> subPerkCheckList =new ArrayList<>();

        List<String> strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/PressTheAttack/PressTheAttack.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/LethalTempo/LethalTempoTemp.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/FleetFootwork/FleetFootwork.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/Conqueror/Conqueror.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/Overheal.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/Triumph.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/PresenceOfMind/PresenceOfMind.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/LegendAlacrity/LegendAlacrity.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/LegendTenacity/LegendTenacity.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/LegendBloodline/LegendBloodline.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/CoupDeGrace/CoupDeGrace.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Precision/CutDown/CutDown.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/LastStand/LastStand.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));
        mainPerkMap.put(8000L,mainPerkCheckList);
        secondaryPerkMap.put(8000L,subPerkCheckList);


        //지배
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();
        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/Electrocute/Electrocute.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/Predator/Predator.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/DarkHarvest/DarkHarvest.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/HailOfBlades/HailOfBlades.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/CheapShot/CheapShot.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/TasteOfBlood/GreenTerror_TasteOfBlood.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/SuddenImpact/SuddenImpact.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/ZombieWard/ZombieWard.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/GhostPoro/GhostPoro.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/EyeballCollection/EyeballCollection.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/TreasureHunter/TreasureHunter.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/IngeniousHunter/IngeniousHunter.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/RelentlessHunter/RelentlessHunter.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Domination/UltimateHunter/UltimateHunter.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));
        mainPerkMap.put(8100L,mainPerkCheckList);
        secondaryPerkMap.put(8100L,subPerkCheckList);


        //결의
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();
        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/GraspOfTheUndying/GraspOfTheUndying.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/VeteranAftershock/VeteranAftershock.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Guardian/Guardian.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Demolish/Demolish.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/FontOfLife/FontOfLife.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/MirrorShell/MirrorShell.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Conditioning/Conditioning.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/SecondWind/SecondWind.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/BonePlating/BonePlating.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Overgrowth/Overgrowth.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/Revitalize/Revitalize.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Unflinching/Unflinching.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));
        mainPerkMap.put(8400L,mainPerkCheckList);
        secondaryPerkMap.put(8400L,subPerkCheckList);



        //영감

        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();
        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/GlacialAugment/GlacialAugment.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/UnsealedSpellbook/UnsealedSpellbook.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/FirstStrike/FirstStrike.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/HextechFlashtraption/HextechFlashtraption.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/MagicalFootwear/MagicalFootwear.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/PerfectTiming/PerfectTiming.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/FuturesMarket/FuturesMarket.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/MinionDematerializer/MinionDematerializer.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/BiscuitDelivery/BiscuitDelivery.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/CosmicInsight/CosmicInsight.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Resolve/ApproachVelocity/ApproachVelocity.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Inspiration/TimeWarpTonic/TimeWarpTonic.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));
        mainPerkMap.put(8300L,mainPerkCheckList);
        secondaryPerkMap.put(8300L,subPerkCheckList);


        //마법
        mainPerkCheckList =new ArrayList<>();
        subPerkCheckList =new ArrayList<>();
        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/SummonAery/SummonAery.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/ArcaneComet/ArcaneComet.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/PhaseRush/PhaseRush.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/NullifyingOrb/Pokeshield.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/ManaflowBand/ManaflowBand.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/NimbusCloak/6361.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Transcendence/Transcendence.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Celerity/CelerityTemp.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/AbsoluteFocus/AbsoluteFocus.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Scorch/Scorch.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/Waterwalking/Waterwalking.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/Styles/Sorcery/GatheringStorm/GatheringStorm.png_disabled.png");
        mainPerkCheckList.add(new PerkCheck(strList,false));
        subPerkCheckList.add(new PerkCheck(strList,false));
        mainPerkMap.put(8200L,mainPerkCheckList);
        secondaryPerkMap.put(8200L,subPerkCheckList);

        //스탯 룬
        List<PerkCheck> subSubCheckList = new ArrayList<>();
        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsAdaptiveForceIcon.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsAttackSpeedIcon.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsCDRScalingIcon.png_disabled.png");
        subSubCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsAdaptiveForceIcon.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsArmorIcon.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsMagicResIcon.MagicResist_Fix.png_disabled.png");
        subSubCheckList.add(new PerkCheck(strList,false));

        strList = new ArrayList<>();
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsHealthScalingIcon.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsArmorIcon.png_disabled.png");
        strList.add("https://lol-duo-bucket.s3.ap-northeast-2.amazonaws.com/perk-images/StatMods/StatModsMagicResIcon.MagicResist_Fix.png_disabled.png");
        subSubCheckList.add(new PerkCheck(strList,false));


    }

    public class PerkCheck{
        List<String> perkList;
        List<Long> perkIdList;
        Boolean isActive;

        public PerkCheck(List<String> perkList, Boolean isActive) {
            this.perkList = perkList;
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
