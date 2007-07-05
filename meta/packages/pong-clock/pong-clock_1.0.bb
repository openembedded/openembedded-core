DESCRIPTION = "A clock combined with a game of pong"
LICENCE = "GPLv2"

SRC_URI = "file://pong-clock-no-flicker.c"

S = "${WORKDIR}"

do_compile () {
	${CC} -o pong-clock pong-clock-no-flicker.c `pkg-config --cflags --libs x11 xau xdmcp`
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 pong-clock ${D}${bindir}
}