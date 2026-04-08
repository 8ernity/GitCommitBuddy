package com.gitcommitbuddy.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CommitCacheDao_Impl implements CommitCacheDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CommitCacheEntity> __insertionAdapterOfCommitCacheEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public CommitCacheDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCommitCacheEntity = new EntityInsertionAdapter<CommitCacheEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `commit_cache` (`id`,`committedToday`,`todayCommitCount`,`lastCommitTime`,`lastCommitRepo`,`lastCommitMessage`,`currentStreak`,`cachedAt`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CommitCacheEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.getCommittedToday() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getTodayCommitCount());
        if (entity.getLastCommitTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLastCommitTime());
        }
        if (entity.getLastCommitRepo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLastCommitRepo());
        }
        if (entity.getLastCommitMessage() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLastCommitMessage());
        }
        statement.bindLong(7, entity.getCurrentStreak());
        statement.bindLong(8, entity.getCachedAt());
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM commit_cache";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final CommitCacheEntity entity, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCommitCacheEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Flow<CommitCacheEntity> observeCache() {
    final String _sql = "SELECT * FROM commit_cache WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"commit_cache"}, new Callable<CommitCacheEntity>() {
      @Override
      @Nullable
      public CommitCacheEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCommittedToday = CursorUtil.getColumnIndexOrThrow(_cursor, "committedToday");
          final int _cursorIndexOfTodayCommitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "todayCommitCount");
          final int _cursorIndexOfLastCommitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitTime");
          final int _cursorIndexOfLastCommitRepo = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitRepo");
          final int _cursorIndexOfLastCommitMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitMessage");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final CommitCacheEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpCommittedToday;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCommittedToday);
            _tmpCommittedToday = _tmp != 0;
            final int _tmpTodayCommitCount;
            _tmpTodayCommitCount = _cursor.getInt(_cursorIndexOfTodayCommitCount);
            final String _tmpLastCommitTime;
            if (_cursor.isNull(_cursorIndexOfLastCommitTime)) {
              _tmpLastCommitTime = null;
            } else {
              _tmpLastCommitTime = _cursor.getString(_cursorIndexOfLastCommitTime);
            }
            final String _tmpLastCommitRepo;
            if (_cursor.isNull(_cursorIndexOfLastCommitRepo)) {
              _tmpLastCommitRepo = null;
            } else {
              _tmpLastCommitRepo = _cursor.getString(_cursorIndexOfLastCommitRepo);
            }
            final String _tmpLastCommitMessage;
            if (_cursor.isNull(_cursorIndexOfLastCommitMessage)) {
              _tmpLastCommitMessage = null;
            } else {
              _tmpLastCommitMessage = _cursor.getString(_cursorIndexOfLastCommitMessage);
            }
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _result = new CommitCacheEntity(_tmpId,_tmpCommittedToday,_tmpTodayCommitCount,_tmpLastCommitTime,_tmpLastCommitRepo,_tmpLastCommitMessage,_tmpCurrentStreak,_tmpCachedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCache(final Continuation<? super CommitCacheEntity> arg0) {
    final String _sql = "SELECT * FROM commit_cache WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CommitCacheEntity>() {
      @Override
      @Nullable
      public CommitCacheEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCommittedToday = CursorUtil.getColumnIndexOrThrow(_cursor, "committedToday");
          final int _cursorIndexOfTodayCommitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "todayCommitCount");
          final int _cursorIndexOfLastCommitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitTime");
          final int _cursorIndexOfLastCommitRepo = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitRepo");
          final int _cursorIndexOfLastCommitMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitMessage");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final CommitCacheEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpCommittedToday;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCommittedToday);
            _tmpCommittedToday = _tmp != 0;
            final int _tmpTodayCommitCount;
            _tmpTodayCommitCount = _cursor.getInt(_cursorIndexOfTodayCommitCount);
            final String _tmpLastCommitTime;
            if (_cursor.isNull(_cursorIndexOfLastCommitTime)) {
              _tmpLastCommitTime = null;
            } else {
              _tmpLastCommitTime = _cursor.getString(_cursorIndexOfLastCommitTime);
            }
            final String _tmpLastCommitRepo;
            if (_cursor.isNull(_cursorIndexOfLastCommitRepo)) {
              _tmpLastCommitRepo = null;
            } else {
              _tmpLastCommitRepo = _cursor.getString(_cursorIndexOfLastCommitRepo);
            }
            final String _tmpLastCommitMessage;
            if (_cursor.isNull(_cursorIndexOfLastCommitMessage)) {
              _tmpLastCommitMessage = null;
            } else {
              _tmpLastCommitMessage = _cursor.getString(_cursorIndexOfLastCommitMessage);
            }
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _result = new CommitCacheEntity(_tmpId,_tmpCommittedToday,_tmpTodayCommitCount,_tmpLastCommitTime,_tmpLastCommitRepo,_tmpLastCommitMessage,_tmpCurrentStreak,_tmpCachedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
