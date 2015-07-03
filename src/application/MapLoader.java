package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.ho.yaml.Yaml;

import RTC.OGMap;
import RTC.OGMapConfig;
import RTC.OGMapTile;


public class MapLoader {

	static public OGMap loadMap(File yamlfile) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(yamlfile));
		StringBuffer strBuf = new StringBuffer();
		while(true) {
			String r = br.readLine();
			if (r == null) {
				break;
			}
			strBuf.append(r);
			strBuf.append("\n");
			System.out.println(r);
		}
		br.close();
		
		
		HashMap param = (HashMap)Yaml.load(strBuf.toString());//load(new FileReader(f));
		BufferedImage file = ImageIO.read(new File((String)param.get("image")));
		HashMap config = (HashMap)param.get("config");
		OGMap ogMap = new OGMap(new RTC.Time(0, 0), new OGMapConfig(0, 0, 0, 0, new RTC.Pose2D(new RTC.Point2D(0, 0), 0)),
				new OGMapTile());
		ogMap.config.origin.position.x = ((Double)config.get("origin_x")).doubleValue();
		ogMap.config.origin.position.y = ((Double)config.get("origin_y")).doubleValue();
		ogMap.config.origin.heading    = ((Double)config.get("origin_th")).doubleValue();
		ogMap.config.xScale =  ((Double)config.get("xScale")).doubleValue();
		ogMap.config.yScale =  ((Double)config.get("yScale")).doubleValue();
		ogMap.map.row =  ((Integer)config.get("row")).intValue();
		ogMap.map.column =  ((Integer)config.get("column")).intValue();
		ogMap.config.width = file.getWidth();
		ogMap.config.height = file.getHeight();
		ogMap.map.width = file.getWidth();
		ogMap.map.height = file.getHeight();
		ogMap.map.cells = new byte[file.getWidth() * file.getHeight()];
		for(int i = 0;i < file.getHeight();i++) {
			for(int j = 0;j < file.getWidth();j++) {
				byte r = (byte)(0xFF & file.getRGB(j, i));
				ogMap.map.cells[i*file.getWidth() + j] = r;
			}
		}
		
		return ogMap;
	}
}
