package com.example.puzzlegames;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Toast;


public class Game extends Activity{

	private static final String TAG = "Game Name" ;
	
	public static final float numberOfVerticalGrids=4f;
	public static final float numberOfHorizontalGrids=4f;
	public static final int numberOfValidMoves=100;
	
	public static final String KEY_DIFFICULTY = "Easy";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	private int puzzle[] = new int[9 * 9];
	private PuzzleView puzzleView;
	private final int used[][][] = new int[9][9][];
	private final String easyPuzzle[] ={"","","","4","4","","","8","","","2","16","","4","8","32"};
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate" );
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY,DIFFICULTY_EASY);
		getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}
	protected void showKeypadOrError(int x, int y) {
		int tiles[] = getUsedTiles(x, y);
		if (tiles.length == numberOfValidMoves) {
		Toast toast = Toast.makeText(this,
		R.string.no_moves_label, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		} else {
		Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
		Dialog v = new Keypad(this, tiles, puzzleView);
		v.show();
		}
		}
	
	 @Override  
	 public boolean onCreateOptionsMenu(Menu menu) {  
	        // Inflate the menu; this adds items to the action bar if it is present.  
	        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu  
	        return true;  
	 }  
	
	protected boolean setTileIfValid(int x, int y, int value) {
		int tiles[] = getUsedTiles(x, y);
		if (value != 0) {
		for (int tile : tiles) {
		if (tile == value)
		return false;
		}
		}
		setTile(x, y, value);
		calculateUsedTiles();
		return true;
		}
	protected int[] getUsedTiles(int x, int y) {
		return used[x][y];
		}
	
	private void calculateUsedTiles() {
		for (int x = 0; x < numberOfVerticalGrids; x++) {
		for (int y = 0; y < numberOfHorizontalGrids; y++) {
		used[x][y] = calculateUsedTiles(x, y);
		// Log.d(TAG, "used[" + x + "][" + y + "] = "
		// + toPuzzleString(used[x][y]));
		}
		}
		}
	
	private int[] calculateUsedTiles(int x, int y) {
		 int c[] = new int[9];
		 // horizontal
		 for (int i = 0; i < 9; i++) {
		 if (i == y)
		 continue;
		 int t = getTile(x, i);
		 if (t != 0)
		 c[t-  1] = t;
		 }
		 // vertical
		 for (int i = 0; i < 9; i++) {
		 if (i == x)
		 continue;
		 int t = getTile(i, y);
		 if (t != 0)
		 c[t - 1] = t;
		 }
		 // same cell block
		 int startx = (x / 3) * 3;
		 int starty = (y / 3) * 3;
		 for (int i = startx; i < startx + 3; i++) {
		 for (int j = starty; j < starty + 3; j++) {
		 if (i == x && j == y)
		 continue;
		 int t = getTile(i, j);
		 if (t != 0)
		 c[t - 1] = t;
		 }
		 }
		 // compress
		 int nused = 0;
		 for (int t : c) {
		 if (t != 0)
		 nused++;
		 }
		 int c1[] = new int[nused];
		 nused = 0;
		 for (int t : c) {
		 if (t != 0)
		 c1[nused++] = t;
		 }
		 return c1;
		 }
	
	private int[] getPuzzle(int diff) {
		String puz;
		// TODO: Continue last game
		switch (diff) {
		case DIFFICULTY_HARD:
		puz = "Hard";
		break;
		case DIFFICULTY_MEDIUM:
		puz = "Medium";
		break;
		case DIFFICULTY_CONTINUE:
			puz = "Continue";
			break;
		case DIFFICULTY_EASY:
		default:
		puz = easyPuzzle.toString();
		break;
		}
		return fromPuzzleString(puz);
		}
	static private String toPuzzleString(int[] puz) {
		StringBuilder buf = new StringBuilder();
		for (int element : puz) {
		buf.append(element);
		}
		return buf.toString();
		}
	static protected int[] fromPuzzleString(String string) {
		int[] puz = new int[string.length()];
		for (int i = 0; i < puz.length; i++) {
		puz[i] = string.charAt(i) - '0' ;
		}
		return puz;
		}
	
	private int getTile(int x, int y) {
		return puzzle[y * 9 + x];
		}
		private void setTile(int x, int y, int value) {
		puzzle[y * 9 + x] = value;
		}
		
		protected String getTileString(int x, int y) {
			int v = getTile(x, y);
			if (v == 0)
			return "" ;
			else
			return String.valueOf(v);
			}
	
		private static final String PREF_PUZZLE = "puzzle" ;
		protected static final int DIFFICULTY_CONTINUE = -1;
		
		@Override
		protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause" );
		
		}

}
