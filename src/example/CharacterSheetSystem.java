package example;

import java.util.ArrayList;

import bus.CharacterSheetMessage;
import bus.MessageBus;
import bus.Systems;
import ecs.AbstractSystem;
import ecs.EntityController;

public class CharacterSheetSystem extends AbstractSystem {
	
	private static final float HP_GROWTH_RATE = 1 + 0.1f;
	
	public CharacterSheetSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
	}

	@Override
	public void run() {
		update();
	}

	@Override
	protected void update() {
		super.timeMarkStart();
		// message queue
		CharacterSheetMessage message;
		while((message = (CharacterSheetMessage) messageBus.getNextMessage(Systems.CHARACTERSHEET_SYSTEM)) != null) {
			switch(message.getOP()) {
			case SYS_CHARSHEET_LVLUP: 
				break;
			default: System.err.println("Character Sheet operation not implemented");
			}
		}
		super.timeMarkEnd();
	}

	@Override
	protected void cleanUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// TODO Auto-generated method stub
		
	}

}
