inherit native

require sqlite3.inc

DEPENDS = "readline-native ncurses-native"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/sqlite3"

do_install() {
	:
}

PACKAGES = ""
