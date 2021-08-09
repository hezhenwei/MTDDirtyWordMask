package MykesTool;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.net.Administration.*;
import mindustry.world.blocks.storage.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WordMaskPlugin extends Plugin{

    private String[] arrayDirtyWords = {"heck", "b人", "操", "你妈", "我日",
            "他妈",
            "操",
            "我日",
            "shit",
            "fuck",
            "草",
            "傻逼",
            "wocao",
            "sb"
    };
    private List<String> ListDirtyWordsExt = new ArrayList<>();

    private void reloadWords()
    {
        ListDirtyWordsExt.clear();
        String strFileName = "config/DirtyWords.txt";
        FileReader fr = null;
        try {
            fr = new FileReader(strFileName);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
        if (fr != null) {
           //System.out.println("[MTDDirtyWordMask] Reading word mask configs.");
            Log.info("[MTDDirtyWordMask] Reading word mask configs.");
            BufferedReader br=new BufferedReader(fr);
            String line="";
            int nCount = 0;
            do{
                try {
                    line=br.readLine();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                if( line != null) {
                    nCount++;
                    ListDirtyWordsExt.add(line);
                    //System.out.println(line);
                }
            } while (line!=null);
            //System.out.println("[MTDDirtyWordMask] Read " + Integer.toString(nCount) + " words.");
            Log.info("[MTDDirtyWordMask] Read " + Integer.toString(nCount) + " words.");
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File f1 = new File(strFileName);
                f1.createNewFile();
                //System.out.println("[MTDDirtyWordMask] Create " +strFileName + " to store dirty phrases.");
                Log.info("[MTDDirtyWordMask] Create " +strFileName + " to store dirty phrases.");
            } catch (Exception e) {
                //System.out.println("[MTDDirtyWordMask] Create " +strFileName + " failed" + e.toString());
                Log.info("[MTDDirtyWordMask] Error! Create " +strFileName + " failed" + e.toString());
            }
        }
    }
    //called when game initializes
    @Override
    public void init(){
        /*
        //listen for a block selection event
        Events.on(BuildSelectEvent.class, event -> {
            if(!event.breaking && event.builder != null && event.builder.buildPlan() != null && event.builder.buildPlan().block == Blocks.thoriumReactor && event.builder.isPlayer()){
                //player is the unit controller
                Player player = event.builder.getPlayer();

                //send a message to everyone saying that this player has begun building a reactor
                Call.sendMessage("[scarlet]ALERT![] " + player.name + " has begun building a reactor at " + event.tile.x + ", " + event.tile.y);
            }
        });
        */

        this.reloadWords();
        //add a chat filter that changes the contents of all messages
        //in this case, all instances of "heck" are censored
        Vars.netServer.admins.addChatFilter((player, text) -> {
            int size = arrayDirtyWords.length;
            for (String strToReplace : arrayDirtyWords) {
                int nPhraseLength = strToReplace.length();
                StringBuilder sb=new StringBuilder();
                for(int j=0;j<nPhraseLength;j++) {
                    sb.append("*");
                }
                text = text.replace(strToReplace, sb.toString());
            }
                for (String strToReplace : ListDirtyWordsExt) {
                    int nPhraseLength = strToReplace.length();
                    StringBuffer sb=new StringBuffer();
                    for(int j=0;j<nPhraseLength;j++) {
                        sb.append("*");
                    }
                    text = text.replace(strToReplace, sb.toString());
                }
            //player.sendMessage("try to do something for v008");
            return text;
        });
        //Vars.netServer.admins.addChatFilter((player, text) -> text.replace("111", "****"));

        /*
        //add an action filter for preventing players from doing certain things
        Vars.netServer.admins.addActionFilter(action -> {
            //random example: prevent blast compound depositing
            if(action.type == ActionType.depositItem && action.item == Items.blastCompound && action.tile.block() instanceof CoreBlock){
                action.player.sendMessage("Example action filter: Prevents players from depositing blast compound into the core.");
                return false;
            }
            return true;
        });

         */
    }

    //register commands that run on the server
    /* // sample
    @Override
    public void registerServerCommands(CommandHandler handler){
        handler.register("reloadwordmask", "Reload word mask in config/DirtyWords.txt.", args -> {
            for(int x = 0; x < Vars.world.width(); x++){
                for(int y = 0; y < Vars.world.height(); y++){
                    //loop through and log all found reactors
                    //make sure to only log reactor centers
                    if(Vars.world.tile(x, y).block() == Blocks.thoriumReactor && Vars.world.tile(x, y).isCenter()){
                        Log.info("Reactor at @, @", x, y);
                    }
                }
            }
        });
    }
    */

    @Override
    public void registerServerCommands(CommandHandler handler){
        handler.register("reloadwordmask", "Reload word mask in config/DirtyWords.txt.", args -> {
            this.reloadWords();
        });
    }

    /*
    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){

        //register a simple reply command
        handler.<Player>register("reply", "<text...>", "A simple ping command that echoes a player's text.", (args, player) -> {
            player.sendMessage("You said: [accent] " + args[0]);
        });

        //register a whisper command which can be used to send other players messages
        handler.<Player>register("whisper", "<player> <text...>", "Whisper text to another player.", (args, player) -> {
            //find player by name
            Player other = Groups.player.find(p -> p.name.equalsIgnoreCase(args[0]));

            //give error message with scarlet-colored text if player isn't found
            if(other == null){
                player.sendMessage("[scarlet]No player by that name found!");
                return;
            }

            //send the other player a message, using [lightgray] for gray text color and [] to reset color
            other.sendMessage("[lightgray](whisper) " + player.name + ":[] " + args[1]);
        });
    }

     */
}
