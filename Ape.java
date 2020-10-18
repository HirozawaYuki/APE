import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class Ape extends JFrame implements ActionListener {
    static double[][] data;

    // この部分は本来はArduino UNOの土壌水分量センサから取得
    // static double current_moisture = 250;    //今の水分量
    static double current_moisture = 180;
    // static double current_moisture = 150;

    static double[] needed_moisture = {180, 240, 150, 100, 220};    // 水やりをするタイミング

    String selectedVegetable = "イチゴ";
    String[] vegetable = {"イチゴ", "ニンジン", "ナス", "ポテト", "トマト"};
    JComboBox<String> combo = new JComboBox<String>(vegetable);
    JButton button;
    JLabel label = new JLabel();

    ImageIcon icon0 = new ImageIcon("./images/strawberry.png"),
        icon0_0 = new ImageIcon("./images/strawberry0.png"),
        icon0_1 = new ImageIcon("./images/strawberry20.png"),
        icon0_2 = new ImageIcon("./images/strawberry40.png"),
        icon0_3 = new ImageIcon("./images/strawberry60.png"),
        icon0_4 = new ImageIcon("./images/strawberry80.png"),
        icon0_5 = new ImageIcon("./images/strawberry100.png");
    ImageIcon icon1 = new ImageIcon("./images/carott.png"),
        icon1_0 = new ImageIcon("./images/carott0.png"),
        icon1_1 = new ImageIcon("./images/carott20.png"),
        icon1_2 = new ImageIcon("./images/carott40.png"),
        icon1_3 = new ImageIcon("./images/carott60.png"),
        icon1_4 = new ImageIcon("./images/carott80.png"),
        icon1_5 = new ImageIcon("./images/carott100.png");
    ImageIcon icon2 = new ImageIcon("./images/eggplant.png"),
        icon2_0 = new ImageIcon("./images/eggplant0.png"),
        icon2_1 = new ImageIcon("./images/eggplant20.png"),
        icon2_2 = new ImageIcon("./images/eggplant40.png"),
        icon2_3 = new ImageIcon("./images/eggplant60.png"),
        icon2_4 = new ImageIcon("./images/eggplant80.png"),
        icon2_5 = new ImageIcon("./images/eggplant100.png");
    ImageIcon icon3 = new ImageIcon("./images/potato.png"),
        icon3_0 = new ImageIcon("./images/potato0.png"),
        icon3_1 = new ImageIcon("./images/potato20.png"),
        icon3_2 = new ImageIcon("./images/potato40.png"),
        icon3_3 = new ImageIcon("./images/potato60.png"),
        icon3_4 = new ImageIcon("./images/potato80.png"),
        icon3_5 = new ImageIcon("./images/potato100.png");
    ImageIcon icon4 = new ImageIcon("./images/tomato.png"),
        icon4_0 = new ImageIcon("./images/tomato0.png"),
        icon4_1 = new ImageIcon("./images/tomato20.png"),
        icon4_2 = new ImageIcon("./images/tomato40.png"),
        icon4_3 = new ImageIcon("./images/tomato60.png"),
        icon4_4 = new ImageIcon("./images/tomato80.png"),
        icon4_5 = new ImageIcon("./images/tomato100.png");


   public static void main(String args[]) {
        Ape frame = new Ape("APE");
        frame.setResizable(false);  // ウィンドウのサイズ変更が可能か否か
        frame.setVisible(true);

        // 指定したフォルダ内のファイルを取得
        File file = new File("./textFile");
        File files[] = file.listFiles();

        // 取得したデータの格納先
        data =new double[files.length][];

        // 全てのファイルに対してデータの格納を行う
        for(int i=0;i<files.length;i++) {
            data[i] = readFiles(files[i].getPath());
        }
   } 

    Ape(String title) {
        setTitle(title);
        setBounds(0, 0, 600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());

        /*------------------------------------------------------*/
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(600, 450));
        p1.setBackground(Color.BLUE);

        label.setIcon(icon0);

        p1.add(label);

        /*------------------------------------------------------*/
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(350, 100));
        p2.setBackground(Color.ORANGE);

        combo.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 50));
        combo.setPreferredSize(new Dimension(300, 50));

        combo.addActionListener(this);

        p2.add(combo);

        /*------------------------------------------------------*/
        JPanel p3 = new JPanel();
        p3.setPreferredSize(new Dimension(300, 100));
        p3.setBackground(Color.GREEN);

        button = new JButton("測定開始");   // テキスト付ボタンの生成
        button.addActionListener(this);
        button.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 70));
        p3.add(button);

        /*------------------------------------------------------*/
        BevelBorder border = new BevelBorder(BevelBorder.RAISED);
        Container contentPane = getContentPane();
        contentPane.add(p1);
        contentPane.add(p2);
        contentPane.add(p3);

    }

    // ボタンとコンボボックスに対するアクション
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == combo){
            selectedVegetable = (String)combo.getSelectedItem();
            if(selectedVegetable == "イチゴ")
            label.setIcon(icon0);
            if(selectedVegetable == "ニンジン")
            label.setIcon(icon1);
            if(selectedVegetable == "ナス")
            label.setIcon(icon2);
            if(selectedVegetable == "ポテト")
            label.setIcon(icon3);
            if(selectedVegetable == "トマト")
            label.setIcon(icon4);
        }

        if(e.getSource() == button){
            changeImages();
            searcher();
        }
    }

    // ファイルを読み込んでString型で返す
    public static double[] readFiles(String fileName){
        String origin_data = "";
        try{
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);

        int ch;
        while((ch = fileReader.read()) != -1){
            origin_data += (char)ch;
        }
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }

        // 改行されているところで区切る
        String[] separated_data = origin_data.split("\n", 0);

        // String型からdouble型への変換
        double[] data = new double[separated_data.length];
        for(int i=0;i<separated_data.length;i++) {
            data[i] = Double.parseDouble(separated_data[i]);
        }
        return data;
    }

  //valueに至るまでのデータの検索とダイアログの表示
    public void searcher(){
        int cnt = 0,//減少に要した時間のカウンタ
            effective_cnt = 0,//減少に要した有効な時間のカウンタ
            effective_data = 0;//有効なファイル数のカウンタ
        double moisture = 0.0;
        boolean start_flag = false;
        System.out.println(selectedVegetable);
        System.out.println();

        if(selectedVegetable=="イチゴ")
        moisture = needed_moisture[0];
        if(selectedVegetable=="ニンジン")
        moisture = needed_moisture[1];
        if(selectedVegetable=="ナス")
        moisture = needed_moisture[2];
        if(selectedVegetable=="ポテト")
        moisture = needed_moisture[3];
        if(selectedVegetable=="トマト")
        moisture = needed_moisture[4];

        for(int i = 0; i < data.length; i++){//ファイルのfor文
        cnt = 0;
        start_flag = false;
            for(int j = data[i].length-1; j > 0; j--){//データのfor文
                // System.out.println(moisture-data[i][j]);
            // System.out.println(Math.abs(moisture-data[i][j]));
            if(!start_flag){
                // if(moisture <= data[i][j]){
            //     start_flag = true;
            //   }
            if(Math.abs(moisture-data[i][j])<3.0){
                start_flag = true;
                // System.out.printf("%d番目のファイル%d",i,j);
            }
            }

            if(start_flag){
            // System.out.println(i + "番目のファイルで" + (current_moisture-data[i][j]));
                    if(current_moisture < data[i][j]){
            // if(Math.abs(current_moisture-data[i][j])<5.0){
                // System.out.println(":" + j);
                effective_data++;
                effective_cnt+=cnt;
                // System.out.println(cnt);
                cnt = 0;
                start_flag = false;
                // System.out.println(effective_data + ":" + effective_cnt);
                // System.out.println(j);
            }else cnt+=5;//一行で5
                }
            }
        }

        int moisture_average = 0;
        if(effective_data!=0)
        moisture_average = effective_cnt/effective_data;
        // System.out.println(moisture_average);

        if(moisture_average == 0){
        JLabel label = new JLabel("今すぐ水やりが必要です");
        JOptionPane.showMessageDialog(this, label);
        // System.out.println("乾燥までおよそ" + cnt + "分");
        // break;
        }else{
        //System.out.println("データ数:" + effective_data);
        //System.out.println("データ:" + effective_cnt);
        int day = moisture_average/60/24,
            hour = (moisture_average-day*60*24)/60,
            minute = moisture_average - day*60*24 - hour*60;
        JLabel label = new JLabel("乾燥までの時間:およそ"+ day + "日" + hour + "時間" + minute + "分");
        JOptionPane.showMessageDialog(this, label);
        // System.out.println("乾燥までおよそ" + cnt + "分");
        // break;
        }
    }

    public void changeImages(){
        //今までに測った水分量の最小値を探す
        double soil_moisture = Double.MAX_VALUE;
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data[i].length; j++){
            if(soil_moisture > data[i][j]){
                soil_moisture = data[i][j];
                //System.out.println(data[i][j]);
            }
            }
        }
        
        int num = 0;
        double moisture = 0.0;
        if(selectedVegetable=="イチゴ")
            moisture = needed_moisture[0];
        if(selectedVegetable=="ニンジン")
            moisture = needed_moisture[1];
        if(selectedVegetable=="ナス")
            moisture = needed_moisture[2];
        if(selectedVegetable=="ポテト")
            moisture = needed_moisture[3];
        if(selectedVegetable=="トマト")
            moisture = needed_moisture[4];
        //System.out.println(moisture-soil_moisture);

        if(current_moisture < moisture){
            double block = (moisture - soil_moisture)/5;
            //System.out.println(block);

            if(current_moisture > (moisture - block))
                num = 1;
            else if(current_moisture > (moisture - block*2))
                num = 2;
            else if(current_moisture > (moisture - block*3))
                num = 3;
            else if(current_moisture > (moisture - block*4))
                num = 4;
            else if(current_moisture > (moisture - block*5))
                num = 5;
            else num = 5;
        }
        else num = 0;
        //System.out.println(num);

        // 選んだ植物、水やり状況に応じて、表示するアイコンを変更
        if(selectedVegetable=="イチゴ"){
            switch(num){
            case 0: label.setIcon(icon0_5);break;
            case 1: label.setIcon(icon0_4);break;
            case 2: label.setIcon(icon0_3);break;
            case 3: label.setIcon(icon0_2);break;
            case 4: label.setIcon(icon0_1);break;
            case 5: label.setIcon(icon0_0);break;
            }
        }
        if(selectedVegetable=="ニンジン"){
            switch(num){
            case 0: label.setIcon(icon1_5);break;
            case 1: label.setIcon(icon1_4);break;
            case 2: label.setIcon(icon1_3);break;
            case 3: label.setIcon(icon1_2);break;
            case 4: label.setIcon(icon1_1);break;
            case 5: label.setIcon(icon1_0);break;
            }
        }
        if(selectedVegetable=="ナス"){
            switch(num){
            case 0: label.setIcon(icon2_5);break;
            case 1: label.setIcon(icon2_4);break;
            case 2: label.setIcon(icon2_3);break;
            case 3: label.setIcon(icon2_2);break;
            case 4: label.setIcon(icon2_1);break;
            case 5: label.setIcon(icon2_0);break;
            }
        }
        if(selectedVegetable=="ポテト"){
            switch(num){
            case 0: label.setIcon(icon3_5);break;
            case 1: label.setIcon(icon3_4);break;
            case 2: label.setIcon(icon3_3);break;
            case 3: label.setIcon(icon3_2);break;
            case 4: label.setIcon(icon3_1);break;
            case 5: label.setIcon(icon3_0);break;
            }
        }
        if(selectedVegetable=="トマト"){
            switch(num){
            case 0: label.setIcon(icon4_5);break;
            case 1: label.setIcon(icon4_4);break;
            case 2: label.setIcon(icon4_3);break;
            case 3: label.setIcon(icon4_2);break;
            case 4: label.setIcon(icon4_1);break;
            case 5: label.setIcon(icon4_0);break;
            }
        }
    }
}
