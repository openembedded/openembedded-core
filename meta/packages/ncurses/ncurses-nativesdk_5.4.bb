require ncurses_${PV}.bb
inherit nativesdk
EXTRA_OEMAKE = '"BUILD_CCFLAGS=${BUILD_CCFLAGS}"'
