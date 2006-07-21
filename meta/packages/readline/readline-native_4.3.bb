include readline_${PV}.bb
inherit native
DEPENDS = "ncurses-native"
FILESPATH = "${FILE_DIRNAME}/readline-${PV}:${FILE_DIRNAME}/readline:${FILE_DIRNAME}/files:${FILE_DIRNAME}"
