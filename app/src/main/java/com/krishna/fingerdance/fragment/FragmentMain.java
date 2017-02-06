package com.krishna.fingerdance.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.TextView;

import com.krishna.fingerdance.R;
import com.krishna.fingerdance.model.Player;
import com.krishna.fingerdance.view.TileView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by krishna on 06/02/17.
 */

public class FragmentMain extends Fragment implements TileView.OnTouchDownListener, TileView.OnTouchUpListener, ViewTreeObserver.OnGlobalLayoutListener {
    private static final String ARG_COLUMN = "column";
    private static final String ARG_ROW = "row";
    private GridLayout gridLayout;
    private TextView tvPlayer;
    private TileView boardTiles[];
    private int totalRows;
    private int totalColumns;

    public int randomRow, randomCol;
    private boolean onTouchUp = false;
    private boolean gameOver = false;
    int currentPlayer = Player.PLAYER_1;
    int maxTouchAllowed = 2;
    Player player1, player2;
    ArrayList<TileView> highlightedTiles;
    private DataCommunicator communicator;

    public static FragmentMain newInstance(int rows, int columns) {
        Bundle args = new Bundle();
        args.putInt(ARG_ROW, rows);
        args.putInt(ARG_COLUMN, columns);
        FragmentMain fragment = new FragmentMain();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        gridLayout = (GridLayout) layout.findViewById(R.id.grid_layout);
        tvPlayer = (TextView) layout.findViewById(R.id.tv_player);
        highlightedTiles = new ArrayList<>();
        Bundle bundle = getArguments();
        totalColumns = bundle.getInt(ARG_COLUMN, 5);
        totalRows = bundle.getInt(ARG_ROW, 5);
        gridLayout.setColumnCount(totalColumns);
        gridLayout.setRowCount(totalRows);

        getMaxTouchAllowed();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        populateBoard();

        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        highlightRandomTile();

        return layout;
    }

    private void populateBoard() {
        boardTiles = new TileView[totalColumns * totalRows];
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalColumns; col++) {
                TileView tileView = new TileView(getActivity(), row, col);
                tileView.setOnTouchDownListener(this);
                tileView.setOnTouchUpListener(this);
                boardTiles[row * totalColumns + col] = tileView;
                gridLayout.addView(tileView);
            }
        }
    }

    private void getMaxTouchAllowed() {
        PackageManager pm = getActivity().getPackageManager();
        boolean hasMultitouch = pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
        if (hasMultitouch) {
            if (pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND)) {
                maxTouchAllowed = 5;
            } else if (pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT))
                maxTouchAllowed = 2;
            else if (pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH))
                maxTouchAllowed = 2;
        }
    }

    @Override
    public void onGlobalLayout() {
        int margin = (int) getResources().getDimension(R.dimen.tileMargin);
        int layoutWidth = gridLayout.getWidth();
        int layoutHeight = gridLayout.getHeight();
        int tileWidth = layoutWidth / totalColumns;
        int tileHeight = layoutHeight / totalRows;
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalColumns; col++) {
                GridLayout.LayoutParams params = (GridLayout.LayoutParams) boardTiles[row * totalColumns + col].getLayoutParams();
                params.width = tileWidth - 2 * margin;
                params.height = tileHeight - 2 * margin;
                params.setMargins(margin, margin, margin, margin);
                boardTiles[row * totalColumns + col].setLayoutParams(params);
            }
        }
    }

    @Override
    public void onTouchDown(TileView tileView, int player) {
        highlightedTiles.add(tileView);
        Player playerRef;
        if (player == Player.PLAYER_1)
            playerRef = player1;
        else
            playerRef = player2;

        playerRef.row = tileView.getTileRow();
        playerRef.column = tileView.getTileColumn();
        playerRef.currentlySelectedTile = tileView;

        if (!onTouchUp) {
            if (tileView.getTileColumn() == randomCol && tileView.getTileRow() == randomRow) {
                playerRef.score += 1;
                if ((player1.score > maxTouchAllowed / 2)) {
                    showScoreDialog(playerRef, "player 1 is allowed only " + (maxTouchAllowed / 2) + "+ fingers at a time.\nGAME OVER");
                    gameOver = true;
                    return;
                } else if (player2.score > maxTouchAllowed / 2) {
                    showScoreDialog(playerRef, "player 2 is allowed only " + (maxTouchAllowed / 2) + "+ fingers at a time.\nGAME OVER");
                    gameOver = true;
                    return;
                }
                highlightRandomTile();
            } else {
                gameOver = true;
                if (currentPlayer == Player.PLAYER_1)
                    showScoreDialog(player2, null);
                else
                    showScoreDialog(player1, null);
            }
        }
    }

    public void highlightRandomTile() {
        Player turn = currentPlayer == Player.PLAYER_1 ? player1 : player2;
        tvPlayer.setText(turn.name + getString(R.string.txt_player_turn));

        TileView randomTile = getRandomTile();
        randomTile.setColor(true);
        randomTile.setPlayer(currentPlayer);
        randomTile.invalidate();
        currentPlayer = (currentPlayer == Player.PLAYER_1) ? Player.PLAYER_2 : Player.PLAYER_1;
    }

    public TileView getRandomTile() {
        Random random = new Random();
        randomRow = random.nextInt(totalRows);
        randomCol = random.nextInt(totalColumns);
        TileView view = boardTiles[randomRow * totalColumns + randomCol];
        if (!highlightedTiles.contains(view))
            return view;
        return getRandomTile();
    }

    @Override
    public void onTouchUp(TileView tileView, int player) {
        if (gameOver || onTouchUp) return;
        Player playerRef = (player == Player.PLAYER_1) ? player1 : player2;
        if (player == Player.PLAYER_1) {
            if (tileView.getTileRow() == playerRef.row && tileView.getTileColumn() == playerRef.column) {
                showScoreDialog(playerRef, null);
                onTouchUp = true;
            } else {
                if (highlightedTiles.contains(tileView))
                    highlightedTiles.remove(tileView);
                onTouchUp = false;
                playerRef.score -= 1;
            }
        }
    }

    public void showScoreDialog(Player player, String message) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_score);
        TextView winner = (TextView) dialog.findViewById(R.id.tvWinner);
        if (message == null)
            message = (player == player1) ? getString(R.string.txt_player1_lost) : getString(R.string.txt_player2_lost) + getString(R.string.txt_game_over);
        else {
            TextView player1Score = (TextView) dialog.findViewById(R.id.player1_score);
            TextView player2Score = (TextView) dialog.findViewById(R.id.player2_score);
            player1Score.setText("Player1 : " + player1.score);
            player2Score.setText("Player2 : " + player2.score);
        }
        winner.setText(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                communicator.onGameOver();
                removeGlobalListener();
            }
        });
        dialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeGlobalListener();
    }

    private void removeGlobalListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        else
            gridLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (DataCommunicator) context;
    }

    public interface DataCommunicator {
        void onGameOver();
    }
}
