require ncurses_${PV}.bb
inherit sdk
EXTRA_OEMAKE = '"BUILD_CCFLAGS=${BUILD_CCFLAGS}"'
