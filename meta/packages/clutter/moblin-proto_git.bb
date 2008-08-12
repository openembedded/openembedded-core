
DEPENDS = "clutter-box2d"

PV = "0.0+git${SRCREV}"

SRC_URI = "git://moblin.org/repos/users/pippin/prototype.git/;protocol=http \
           file://fix.patch;patch=1"

S = "${WORKDIR}/git"

do_install () {
	install -d ${D}${bindir}
	install ${S}/moblin-proto ${D}${bindir}
}
