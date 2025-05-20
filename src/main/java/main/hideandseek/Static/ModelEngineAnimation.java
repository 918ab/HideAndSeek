package main.hideandseek.Static;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.ModelState;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.entity.data.BukkitEntityData;
import com.ticxo.modelengine.api.entity.data.IEntityData;
import com.ticxo.modelengine.api.generator.blueprint.ModelBlueprint;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.bone.BoneBehaviorTypes;
import com.ticxo.modelengine.api.model.bone.type.PlayerLimb;
import com.ticxo.modelengine.core.command.ModelOptionParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileReader;
import java.util.List;
public class ModelEngineAnimation {

    private static final String pr = "§x§0§0§E§2§2§2H§x§0§0§E§5§3§7i§x§0§0§E§8§4§Cd§x§0§0§E§B§6§1e§x§0§0§E§E§7§6A§x§0§0§F§1§8§Cn§x§0§0§F§3§A§1d§x§0§0§F§6§B§6S§x§0§0§F§9§C§Be§x§0§0§F§C§E§0e§x§0§0§F§F§F§5k §f>> ";

      public static AnimationHandler getHandler(Player player) {
        ModelEngineAPI api = ModelEngineAPI.getAPI();
        if (api == null) {
            Bukkit.broadcastMessage(pr + "ModelEngineAPI 연결 실패");
            return null;
        }

        ModeledEntity entity = api.getModelUpdaters().getModeledEntity(player.getUniqueId());
        if (entity == null) {
            player.sendMessage(pr + "적용된 모델 없음");
            return null;
        }

        ActiveModel model = entity.getModels().values().stream().findFirst().orElse(null);
        if (model == null) {
            player.sendMessage(pr + "적용된 모델 없음");
            return null;
        }

        return model.getAnimationHandler();
    }
    public static void Stuck(Player player) {
        AnimationHandler handler = getHandler(player);
        if (handler == null) return;
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.IDLE, "idle", 0.2, 0.2, 1.0));
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.WALK, "idle", 0.2, 0.2, 1.0));
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP, "idle", 0.2, 0.2, 1.0));
    }
    public static void unStuck(Player player) {
        AnimationHandler handler = getHandler(player);
        if (handler == null) return;
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.IDLE, "idle", 0.2, 0.2, 1.0));
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.WALK, "walk", 0.2, 0.2, 1.0));
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP, "jump", 0.2, 0.2, 1.0));
    }


    public static void undisguisePlayer(Player player){
        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(player.getUniqueId());
        if(modeledEntity != null) {
            player.getInventory().clear();
            player.setMaxHealth(20);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(20.0f);
            HideAndSeekStorage.remove("[Player]"+player.getName()+",Animation");
            HideAndSeekStorage.remove("[Player]"+player.getName()+",Model");
            player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
            modeledEntity.markRemoved();
            ModelEngineAPI.getEntityHandler().setForcedInvisible(player, false);
            ModelEngineAPI.getEntityHandler().forceSpawn(player);
        }
    }
    public static void disguisePlayer(Player player, String modelId) {
        ModelBlueprint blueprint = ModelEngineAPI.getBlueprint(modelId);
        if (blueprint == null) {
            return;
        }
        ModeledEntity modeledEntity = ModelEngineAPI.getOrCreateModeledEntity(player);
        modeledEntity.removeModel(modelId).ifPresent(ActiveModel::destroy);
        modeledEntity.getBase().getBodyRotationController().setPlayerMode(true);
        modeledEntity.setBaseEntityVisible(false);
        IEntityData data = modeledEntity.getBase().getData();
        if (data instanceof BukkitEntityData) {
            BukkitEntityData bukkitData = (BukkitEntityData) data;
            bukkitData.getTracked().addForcedPairing(player.getUniqueId());
        }
        ModelEngineAPI.getEntityHandler().setForcedInvisible(player, true);
        ActiveModel activeModel = ModelEngineAPI.createActiveModel(blueprint);
        String input = "scale "+HideAndSeekStorage.get(modelId+",ModelScale")+" hitboxScale "+HideAndSeekStorage.get(modelId+",HitboxScale");
        String[] args = input.split(" ");
        ModelOptionParser options = ModelOptionParser.parse(0, args);
        options.applyDisguiseOptions(activeModel);
        modeledEntity.addModel(activeModel, false);
        activeModel.getBones().values().forEach(modelBone -> {
            modelBone.getBoneBehavior(BoneBehaviorTypes.PLAYER_LIMB).ifPresent(behavior -> {
                ((PlayerLimb) behavior).setTexture(player);
            });
        });
        player.setMaxHealth(Double.parseDouble(HideAndSeekStorage.get(modelId+",PlayerHealth").toString()));
        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(Double.parseDouble(HideAndSeekStorage.get(modelId+",PlayerScale").toString()));
        HideAndSeekStorage.put("[Player]"+player.getName()+",Model",modelId);
        List<String> list = HideAndSeekStorage.getList("[ModelEngine]"+modelId);
        if(list.size() > 32){
            Bukkit.getLogger().info("§c 애니메이션이 너무 많습니다 ("+modelId+")");
            return;
        }
        if(!HideAndSeekStorage.get(modelId+",Name").equals("설정되지않음")){
            player.sendMessage(pr+"§a"+HideAndSeekStorage.get(modelId+",Name")+"§f으(로) 변신했습니다");
        }else{
            player.sendMessage(pr+"변신 완료");
        }
        int slot = 4;
        for(String text : list){
            String Animation = text.replace("[ModelEngine]"+modelId+",","");
            ItemStack item = null;
            if(HideAndSeekStorage.get(text).equals("auto")) {
                item = new ItemBuilder(Material.IRON_NUGGET)
                    .setDisplayName("§f"+Animation)
                    .addLore(" ")
                    .addLore("§x§D§0§D§0§D§0클릭시 애니메이션을 실행합니다.")
                    .setCustomModelData(1)
                    .build();
            }else{
                item = new ItemBuilder(Material.IRON_NUGGET)
                    .setDisplayName("§f"+Animation)
                    .addLore(" ")
                    .addLore("§x§D§0§D§0§D§0클릭시 애니메이션이 실행되며")
                    .addLore("§x§D§0§D§0§D§0다시 클릭하면 원래 상태로 돌아옵니다")
                    .setCustomModelData(3)
                    .build();
            }
            player.getInventory().setItem(slot,item);
            slot++;
        }

    }
    public static void ModelReload() {
        DataManager dataManager = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
        File blueprintDir = new File("plugins/ModelEngine/blueprints/");

        if (!blueprintDir.exists() || !blueprintDir.isDirectory()) {
            Bukkit.broadcastMessage("§c디렉토리가 존재하지 않거나 잘못되었습니다");
            return;
        }

        File[] files = blueprintDir.listFiles();
        if (files == null || files.length == 0) {
            Bukkit.broadcastMessage("§c디렉토리에 파일이 없습니다");
            return;
        }

        for (File file : files) {
            if (!file.getName().endsWith(".bbmodel")) continue;

            String modelId = file.getName().replace(".bbmodel", "");
            String modelPath = "HideAndSeek." + modelId;

            if (dataManager.get(modelPath) == null) {
                Bukkit.getLogger().info("[HideAndSeek] " + modelId + " 자동생성");
                dataManager.set(modelPath + ".Name", "설정되지않음");
                dataManager.set(modelPath + ".PlayerScale", 1);
                dataManager.set(modelPath + ".ModelScale", 1);
                dataManager.set(modelPath + ".HitboxScale", 1);
                dataManager.set(modelPath + ".PlayerHealth", 20);
            }

            HideAndSeekStorage.put(modelId + ",Name", dataManager.get(modelPath + ".Name"));
            HideAndSeekStorage.put(modelId + ",ModelScale", dataManager.get(modelPath + ".ModelScale"));
            HideAndSeekStorage.put(modelId + ",PlayerScale", dataManager.get(modelPath + ".PlayerScale"));
            HideAndSeekStorage.put(modelId + ",HitboxScale", dataManager.get(modelPath + ".HitboxScale"));
            HideAndSeekStorage.put(modelId + ",PlayerHealth", dataManager.get(modelPath + ".PlayerHealth"));


            parseAndApplyAnimations(modelId, dataManager);
        }
    }

    private static void parseAndApplyAnimations(String modelId, DataManager dataManager) {
        File modelFile = new File("plugins/ModelEngine/blueprints/" + modelId + ".bbmodel");
        try (FileReader reader = new FileReader(modelFile)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonElement animationsElement = json.get("animations");

            if (animationsElement == null || !animationsElement.isJsonArray()) {
                Bukkit.broadcastMessage("§canimations 존재하지 않음(" + modelId + ")");
                return;
            }

            JsonArray animations = animationsElement.getAsJsonArray();
            if (animations.size() == 0) {
                Bukkit.broadcastMessage("§c애니메이션 없음(" + modelId + ")");
                return;
            }

            for (JsonElement animationElement : animations) {
                JsonObject animObj = animationElement.getAsJsonObject();
                if (!animObj.has("name") || !animObj.has("length")) continue;

                String animation = animObj.get("name").getAsString();
                double length = animObj.get("length").getAsDouble();
                String loopType = animObj.has("loop") ? animObj.get("loop").getAsString() : "";

                HideAndSeekStorage.put("[Animation]" + modelId + "," + animation, length);

                String mode = loopType.equals("hold") ? "switch" : "auto";
                dataManager.set("HideAndSeek." + modelId + ".Animation." + animation, mode);

                HideAndSeekStorage.put("[ModelEngine]" + modelId + "," + animation, mode);
            }

        } catch (Exception e) {
            Bukkit.broadcastMessage("§c모델 로딩 오류(" + modelId + ") : " + e.getMessage());
        }
    }

}

