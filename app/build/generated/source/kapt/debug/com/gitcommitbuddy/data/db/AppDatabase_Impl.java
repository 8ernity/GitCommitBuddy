package com.gitcommitbuddy.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile CommitCacheDao _commitCacheDao;

  private volatile DailyCommitDao _dailyCommitDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `commit_cache` (`id` INTEGER NOT NULL, `committedToday` INTEGER NOT NULL, `lastCommitTime` TEXT, `lastCommitRepo` TEXT, `lastCommitMessage` TEXT, `currentStreak` INTEGER NOT NULL, `cachedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `daily_commits` (`dateKey` TEXT NOT NULL, `didCommit` INTEGER NOT NULL, `commitCount` INTEGER NOT NULL, `lastCommitTime` TEXT, PRIMARY KEY(`dateKey`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e9fe0b1d9f83e96122043e2b92f06c5f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `commit_cache`");
        db.execSQL("DROP TABLE IF EXISTS `daily_commits`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCommitCache = new HashMap<String, TableInfo.Column>(7);
        _columnsCommitCache.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitCache.put("committedToday", new TableInfo.Column("committedToday", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitCache.put("lastCommitTime", new TableInfo.Column("lastCommitTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitCache.put("lastCommitRepo", new TableInfo.Column("lastCommitRepo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitCache.put("lastCommitMessage", new TableInfo.Column("lastCommitMessage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitCache.put("currentStreak", new TableInfo.Column("currentStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitCache.put("cachedAt", new TableInfo.Column("cachedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCommitCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCommitCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCommitCache = new TableInfo("commit_cache", _columnsCommitCache, _foreignKeysCommitCache, _indicesCommitCache);
        final TableInfo _existingCommitCache = TableInfo.read(db, "commit_cache");
        if (!_infoCommitCache.equals(_existingCommitCache)) {
          return new RoomOpenHelper.ValidationResult(false, "commit_cache(com.gitcommitbuddy.data.db.CommitCacheEntity).\n"
                  + " Expected:\n" + _infoCommitCache + "\n"
                  + " Found:\n" + _existingCommitCache);
        }
        final HashMap<String, TableInfo.Column> _columnsDailyCommits = new HashMap<String, TableInfo.Column>(4);
        _columnsDailyCommits.put("dateKey", new TableInfo.Column("dateKey", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCommits.put("didCommit", new TableInfo.Column("didCommit", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCommits.put("commitCount", new TableInfo.Column("commitCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCommits.put("lastCommitTime", new TableInfo.Column("lastCommitTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDailyCommits = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDailyCommits = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDailyCommits = new TableInfo("daily_commits", _columnsDailyCommits, _foreignKeysDailyCommits, _indicesDailyCommits);
        final TableInfo _existingDailyCommits = TableInfo.read(db, "daily_commits");
        if (!_infoDailyCommits.equals(_existingDailyCommits)) {
          return new RoomOpenHelper.ValidationResult(false, "daily_commits(com.gitcommitbuddy.data.db.DailyCommitEntity).\n"
                  + " Expected:\n" + _infoDailyCommits + "\n"
                  + " Found:\n" + _existingDailyCommits);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e9fe0b1d9f83e96122043e2b92f06c5f", "64d72df8eb15e03d480f2ff490e458f4");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "commit_cache","daily_commits");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `commit_cache`");
      _db.execSQL("DELETE FROM `daily_commits`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CommitCacheDao.class, CommitCacheDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DailyCommitDao.class, DailyCommitDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CommitCacheDao commitCacheDao() {
    if (_commitCacheDao != null) {
      return _commitCacheDao;
    } else {
      synchronized(this) {
        if(_commitCacheDao == null) {
          _commitCacheDao = new CommitCacheDao_Impl(this);
        }
        return _commitCacheDao;
      }
    }
  }

  @Override
  public DailyCommitDao dailyCommitDao() {
    if (_dailyCommitDao != null) {
      return _dailyCommitDao;
    } else {
      synchronized(this) {
        if(_dailyCommitDao == null) {
          _dailyCommitDao = new DailyCommitDao_Impl(this);
        }
        return _dailyCommitDao;
      }
    }
  }
}
