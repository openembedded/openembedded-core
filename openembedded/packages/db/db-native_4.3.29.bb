# This wrapper builds a native version of the SleepyCat
# Berkeley DB for those packages which need it (e.g.
# perl).
SECTION = "libs"
VIRTUAL_NAME = "virtual/db-native"
CONFLICTS = "db3-native"
#PR tracks the non-native package

inherit native

include db_${PV}.bb

PACKAGES = ""
