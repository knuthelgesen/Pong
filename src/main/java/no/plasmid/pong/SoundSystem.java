package no.plasmid.pong;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class SoundSystem {

	public int loadWavFile(String fileName) throws FileNotFoundException {
		URL fileURL = SoundSystem.class.getResource(fileName);
		if (null == fileURL) {
			throw new FileNotFoundException("Could not find file " + fileName);
		}
		int bufferId = AL10.alGenBuffers();
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			throw new IllegalStateException("Could not allocate OpenAL buffer for sound: " + fileName);
		}
		
		WaveData data =  WaveData.create(SoundSystem.class.getResource(fileName));
		AL10.alBufferData(bufferId, data.format, data.data, data.samplerate);
		data.dispose();
		
		return bufferId;
	}
	
	public int createSource() {
		int sourceId = AL10.alGenSources();
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			throw new IllegalStateException("Could not allocate OpenAL buffer for source!");
		}
		
		AL10.alSourcef(sourceId, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 1.0f);
		AL10.alSource3f(sourceId, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
		
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			throw new IllegalStateException("Could not set values on source with ID " + sourceId);
		}

		return sourceId;
	}
	
	public void deleteSource(int sourceId) {
		AL10.alDeleteSources(sourceId);
	}
	
	public void initializeListener() {
		FloatBuffer listenerOrientation = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();
		
		AL10.alListener3f(AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
		AL10.alListener3f(AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOrientation);
	}
	
	public void playSound(int sourceId, int soundId) {
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, soundId);
		AL10.alSourcePlay(sourceId);
	}
	
}
