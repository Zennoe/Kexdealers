package example;

import java.util.HashMap;

public class ResourceLoader {
	
	private HashMap<String, Integer> pointerCounter = new HashMap<>();
	private HashMap<String, AssetData> assets = new HashMap<>();
	
	public ResourceLoader(){
		// preload keys
	}
	
	public void load(String assetName){
		int x = pointerCounter.get(assetName);
		if(x == 0){
			// load fresh from HDD			
		}else{
			x++;
		}
	}
	
	public void unload(String assetName){
		int x = pointerCounter.get(assetName);
		if(x == 1){
			x--;
			//unload completely
			assets.put(assetName, null);
		}else{
			x--;
		}
	}
	
	public AssetData getRessource(String assetName){
		return assets.get(assetName);
	}
}
