package tw.redstonepriority.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tw.redstonepriority.algorithm.PermutationGenerator;
import tw.redstonepriority.data.ComponentRegistry;
import java.math.BigInteger;
import java.util.*;

public class PriorityCalculatorScreen extends Screen {
    private static final int MAX_ROWS = 100_000, COPY_ALL_LIMIT = 2_000;
    private TextFieldWidget input;
    private List<List<String>> rows = List.of();
    private List<String> tokens = List.of();
    private int selected = -1, scroll = 0, visualScroll = 0;
    private String status = "規律模型推算｜不是遊戲實測的通用優先級";
    private int listTop, listBottom, listLeft, listRight, visualTop, visualBottom, visualLeft, visualRight;

    public PriorityCalculatorScreen() { super(Text.literal("紅石優先級排列計算器")); }
    @Override protected void init() {
        int left = 16, width = this.width - 32;
        input = new TextFieldWidget(textRenderer, left, 38, Math.max(80,width-140), 20, Text.literal("輸入元素"));
        input.setMaxLength(512); input.setPlaceholder(Text.literal("例如：01234 或 比較器,中繼器1")); addDrawableChild(input);
        addDrawableChild(ButtonWidget.builder(Text.literal("產生排列"), b -> generate()).dimensions(left+width-132,37,132,22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("複製目前排列"), b -> copySelected()).dimensions(left, this.height-30, 130, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("複製全部排列"), b -> copyAll()).dimensions(left+136, this.height-30, 130, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("清除"), b -> clear()).dimensions(left+272, this.height-30, 70, 20).build());
        if (rows.isEmpty()) input.setText("01234");
    }
    private void generate() {
        try {
            List<String> parsed = PermutationGenerator.parse(input.getText());
            BigInteger count = PermutationGenerator.factorial(parsed.size());
            if (count.compareTo(BigInteger.valueOf(MAX_ROWS)) > 0) { status = "共 "+count+" 筆，超過單次計算上限 100,000；請減少元素。"; rows = List.of(); selected=-1; tokens=parsed; visualScroll=0; return; }
            tokens = parsed; rows = PermutationGenerator.generate(parsed); selected = rows.isEmpty() ? -1 : 0; scroll=0; visualScroll=0;
            status = "元素 "+tokens.size()+" 個｜排列 "+count+" 筆｜規律模型推算";
        } catch (IllegalArgumentException ex) { rows=List.of(); selected=-1; status="錯誤："+ex.getMessage(); }
    }
    private void clear() { input.setText(""); rows=List.of(); tokens=List.of(); selected=-1; scroll=0; visualScroll=0; status="已清除。輸入元素後產生排列。"; }
    private String formatRow(List<String> row) { return String.join(input != null && input.getText().contains(",") ? ", " : "", row); }
    private void copySelected() { if (selected>=0 && selected<rows.size()) client.keyboard.setClipboard(formatRow(rows.get(selected))); }
    private void copyAll() {
        if (rows.isEmpty()) { status="尚無排列結果。"; return; }
        if (rows.size()>COPY_ALL_LIMIT) { status="全部結果共 "+rows.size()+" 筆，為避免卡頓，複製上限為 2,000 筆。"; return; }
        StringJoiner joiner=new StringJoiner(System.lineSeparator()); rows.forEach(row -> joiner.add(formatRow(row))); client.keyboard.setClipboard(joiner.toString()); status="已複製全部 "+rows.size()+" 筆。";
    }
    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta); context.drawCenteredTextWithShadow(textRenderer,title,width/2,12,0xFFFFFF);
        super.render(context,mouseX,mouseY,delta);
        context.drawTextWithShadow(textRenderer,status,16,66,0xFFE08A);
        int x=16, rowH=12, outerGap=10;
        listLeft=x;
        int listWidth=Math.max(140,Math.min(290,(width-52)*36/100));
        listRight=listLeft+listWidth;
        visualLeft=listRight+outerGap;
        visualRight=width-16;
        listTop=96; listBottom=height-40; visualTop=listTop; visualBottom=listBottom;
        int visibleRows=Math.max(0,(listBottom-listTop)/rowH);

        context.drawTextWithShadow(textRenderer,"排列清單",listLeft,listTop-12,0xFFFFFF);
        context.fill(listLeft-2,listTop-2,listRight,listBottom,0x88000000);
        for(int i=scroll;i<Math.min(rows.size(),scroll+visibleRows);i++) {
            int y=listTop+(i-scroll)*rowH;
            if(i==selected) context.fill(listLeft-1,y-1,listRight-1,y+rowH,0xAA365B78);
            String line=textRenderer.trimToWidth((i+1)+". "+formatRow(rows.get(i)),listWidth-8);
            context.drawText(textRenderer,line,listLeft+3,y,0xFFFFFF,false);
        }

        String selectionLabel=rows.isEmpty()?"尚未產生排列。":"第 "+(selected+1)+" / "+rows.size()+" 筆";
        context.drawTextWithShadow(textRenderer,textRenderer.trimToWidth("執行優先順序（規律模型推算）  "+selectionLabel,Math.max(0,visualRight-visualLeft)),visualLeft,listTop-12,0xFFFFFF);
        context.fill(visualLeft-2,visualTop-2,visualRight,visualBottom,0x88000000);
        context.drawTextWithShadow(textRenderer,"由上而下表示先後順序, 滑鼠滾輪可查看全部順位",visualLeft+4,visualTop+3,0xFFCE78);

        if(selected>=0 && selected<rows.size()) {
            List<String> row=rows.get(selected);
            int cardHeight=40, cardGap=12, itemStep=cardHeight+cardGap;
            int firstCardY=visualTop+18;
            int visibleCards=Math.max(1,(visualBottom-firstCardY+cardGap)/itemStep);
            int maxVisualScroll=Math.max(0,row.size()-visibleCards);
            visualScroll=Math.max(0,Math.min(visualScroll,maxVisualScroll));
            context.fill(visualLeft+4,visualTop+17,visualRight-4,visualBottom-4,0x44000000);
            int cardX=visualLeft+6;
            int cardRight=visualRight-8;
            context.enableScissor(visualLeft+2,visualTop+16,visualRight-2,visualBottom-2);
            for(int i=visualScroll;i<Math.min(row.size(),visualScroll+visibleCards);i++) {
                String symbol=row.get(i);
                int cardY=firstCardY+(i-visualScroll)*itemStep;
                int cardWidth=cardRight-cardX;
                if(cardWidth<=0 || cardY+cardHeight>visualBottom-3) break;
                int cardColor=i==0?0xDD3A4E38:0xCC263746;
                int borderColor=i==0?0xFFFFD36A:0xFF6F9AB5;
                context.fill(cardX,cardY,cardRight,cardY+cardHeight,cardColor);
                context.drawBorder(cardX,cardY,cardWidth,cardHeight,borderColor);

                int badgeLeft=cardX+5, badgeTop=cardY+5;
                context.fill(badgeLeft,badgeTop,badgeLeft+30,badgeTop+30,i==0?0xFFB47A20:0xFF365B78);
                context.drawCenteredTextWithShadow(textRenderer,Integer.toString(i+1),badgeLeft+15,badgeTop+11,0xFFFFFF);

                int textX=badgeLeft+38, textWidth=Math.max(0,cardRight-textX-6);
                String rankText=i==0?"順位 1  /  最先執行":"順位 "+(i+1);
                context.drawTextWithShadow(textRenderer,textRenderer.trimToWidth(rankText,textWidth),textX,cardY+4,i==0?0xFFFFD36A:0xFFEAF4FA);
                var component=ComponentRegistry.find(symbol);
                String detail=component==null?"自訂元素  /  "+symbol:component.displayName()+"  /  "+component.delayTicks()+" RT";
                context.drawText(textRenderer,textRenderer.trimToWidth("代號 "+symbol+"  ·  "+detail,textWidth),textX,cardY+18,0xD6E5ED,false);

                if(i<row.size()-1 && i<visualScroll+visibleCards-1) {
                    context.drawCenteredTextWithShadow(textRenderer,"↓",cardX+20,cardY+cardHeight+1,0xFFCE78);
                }
            }
            context.disableScissor();
            if(maxVisualScroll>0) {
                String range="順位 "+(visualScroll+1)+"-"+Math.min(row.size(),visualScroll+visibleCards)+" / "+row.size();
                context.drawTextWithShadow(textRenderer,range,visualRight-textRenderer.getWidth(range)-5,visualBottom-11,0xFFCE78);
            }
        } else {
            context.drawTextWithShadow(textRenderer,"請輸入元素並產生排列。",visualLeft+8,visualTop+28,Formatting.GRAY.getColorValue());
        }
    }
    @Override public boolean mouseClicked(double mouseX,double mouseY,int button) {
        if(button==0 && mouseX>=listLeft && mouseX<listRight && mouseY>=listTop && mouseY<listBottom) {
            int index=scroll+(int)((mouseY-listTop)/12); if(index>=0&&index<rows.size()) { selected=index; visualScroll=0; }
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }
    @Override public boolean mouseScrolled(double mouseX,double mouseY,double horizontalAmount,double verticalAmount) {
        if(mouseX>=listLeft && mouseX<listRight && mouseY>=listTop && mouseY<listBottom && !rows.isEmpty()) {
            int visible=Math.max(1,(listBottom-listTop)/12);
            scroll=Math.max(0,Math.min(rows.size()-visible,scroll+(verticalAmount<0?3:-3)));
            return true;
        }
        if(mouseX>=visualLeft && mouseX<visualRight && mouseY>=visualTop && mouseY<visualBottom && selected>=0 && selected<rows.size()) {
            int cardHeight=40, cardGap=12, firstCardY=visualTop+18;
            int visible=Math.max(1,(visualBottom-firstCardY+cardGap)/(cardHeight+cardGap));
            int maximum=Math.max(0,rows.get(selected).size()-visible);
            visualScroll=Math.max(0,Math.min(maximum,visualScroll+(verticalAmount<0?1:-1)));
            return true;
        }
        return super.mouseScrolled(mouseX,mouseY,horizontalAmount,verticalAmount);
    }
    @Override public boolean shouldPause() { return false; }
}
