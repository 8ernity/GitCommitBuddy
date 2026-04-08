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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DailyCommitDao_Impl implements DailyCommitDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DailyCommitEntity> __insertionAdapterOfDailyCommitEntity;

  private final SharedSQLiteStatement __preparedStmtOfPruneOlderThan;

  public DailyCommitDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDailyCommitEntity = new EntityInsertionAdapter<DailyCommitEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `daily_commits` (`dateKey`,`didCommit`,`commitCount`,`lastCommitTime`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyCommitEntity entity) {
        if (entity.getDateKey() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDateKey());
        }
        final int _tmp = entity.getDidCommit() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getCommitCount());
        if (entity.getLastCommitTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLastCommitTime());
        }
      }
    };
    this.__preparedStmtOfPruneOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM daily_commits WHERE dateKey < ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final DailyCommitEntity entity, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailyCommitEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object pruneOlderThan(final String cutoffDate, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfPruneOlderThan.acquire();
        int _argIndex = 1;
        if (cutoffDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, cutoffDate);
        }
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
          __preparedStmtOfPruneOlderThan.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<DailyCommitEntity>> observeRecentDays() {
    final String _sql = "SELECT * FROM daily_commits ORDER BY dateKey DESC LIMIT 90";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_commits"}, new Callable<List<DailyCommitEntity>>() {
      @Override
      @NonNull
      public List<DailyCommitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfDidCommit = CursorUtil.getColumnIndexOrThrow(_cursor, "didCommit");
          final int _cursorIndexOfCommitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commitCount");
          final int _cursorIndexOfLastCommitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitTime");
          final List<DailyCommitEntity> _result = new ArrayList<DailyCommitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyCommitEntity _item;
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final boolean _tmpDidCommit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDidCommit);
            _tmpDidCommit = _tmp != 0;
            final int _tmpCommitCount;
            _tmpCommitCount = _cursor.getInt(_cursorIndexOfCommitCount);
            final String _tmpLastCommitTime;
            if (_cursor.isNull(_cursorIndexOfLastCommitTime)) {
              _tmpLastCommitTime = null;
            } else {
              _tmpLastCommitTime = _cursor.getString(_cursorIndexOfLastCommitTime);
            }
            _item = new DailyCommitEntity(_tmpDateKey,_tmpDidCommit,_tmpCommitCount,_tmpLastCommitTime);
            _result.add(_item);
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
  public Object getRecentDays(final Continuation<? super List<DailyCommitEntity>> arg0) {
    final String _sql = "SELECT * FROM daily_commits ORDER BY dateKey DESC LIMIT 90";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DailyCommitEntity>>() {
      @Override
      @NonNull
      public List<DailyCommitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfDidCommit = CursorUtil.getColumnIndexOrThrow(_cursor, "didCommit");
          final int _cursorIndexOfCommitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commitCount");
          final int _cursorIndexOfLastCommitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitTime");
          final List<DailyCommitEntity> _result = new ArrayList<DailyCommitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyCommitEntity _item;
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final boolean _tmpDidCommit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDidCommit);
            _tmpDidCommit = _tmp != 0;
            final int _tmpCommitCount;
            _tmpCommitCount = _cursor.getInt(_cursorIndexOfCommitCount);
            final String _tmpLastCommitTime;
            if (_cursor.isNull(_cursorIndexOfLastCommitTime)) {
              _tmpLastCommitTime = null;
            } else {
              _tmpLastCommitTime = _cursor.getString(_cursorIndexOfLastCommitTime);
            }
            _item = new DailyCommitEntity(_tmpDateKey,_tmpDidCommit,_tmpCommitCount,_tmpLastCommitTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getForDate(final String dateKey,
      final Continuation<? super DailyCommitEntity> arg1) {
    final String _sql = "SELECT * FROM daily_commits WHERE dateKey = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dateKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, dateKey);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DailyCommitEntity>() {
      @Override
      @Nullable
      public DailyCommitEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfDidCommit = CursorUtil.getColumnIndexOrThrow(_cursor, "didCommit");
          final int _cursorIndexOfCommitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commitCount");
          final int _cursorIndexOfLastCommitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCommitTime");
          final DailyCommitEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final boolean _tmpDidCommit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDidCommit);
            _tmpDidCommit = _tmp != 0;
            final int _tmpCommitCount;
            _tmpCommitCount = _cursor.getInt(_cursorIndexOfCommitCount);
            final String _tmpLastCommitTime;
            if (_cursor.isNull(_cursorIndexOfLastCommitTime)) {
              _tmpLastCommitTime = null;
            } else {
              _tmpLastCommitTime = _cursor.getString(_cursorIndexOfLastCommitTime);
            }
            _result = new DailyCommitEntity(_tmpDateKey,_tmpDidCommit,_tmpCommitCount,_tmpLastCommitTime);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
