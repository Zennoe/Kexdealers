package bus;

// deprecated
public enum Operation {
	// EntityController
	// ResourceLoader
	// RenderSystem
	SYS_RENDER_WIREFRAME_ON,
	SYS_RENDER_WIREFRAME_OFF,
	SYS_RENDER_DEBUGLINES_ON,
	SYS_RENDER_DEBUGLINES_OFF,
	SYS_RENDER_DEBUGLINES_ADDNEWLINE,
	// AudioSystem
	SYS_AUDIO_MUTE,
	// NetworkSystem
	SYS_NETWORK_CONNECT,
	SYS_NETWORK_DISCONNECT,
	// TeleportationSystem
	SYS_TELEPORTATION_TARGETCOORDS,
	// CharacterSheetSystem
	SYS_CHARSHEET_LVLUP,
	// Player
	PLAYER_MOVE,
	PLAYER_LOOK,
	PLAYER_JUMP,
	PLAYER_INTERACT,
}
