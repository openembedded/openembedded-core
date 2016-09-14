require cmake.inc
inherit native

# Using cmake's internal libarchive, so some dependencies are needed
DEPENDS += "bzip2-native zlib-native"

SRC_URI += "\
    file://cmlibarchive-disable-ext2fs.patch \
"

B = "${WORKDIR}/build"
do_configure[cleandirs] = "${B}"

# Disable ccmake since we don't depend on ncurses
CMAKE_EXTRACONF = "\
    -DBUILD_CursesDialog=0 \
    -DENABLE_ACL=0 -DHAVE_ACL_LIBACL_H=0 \
    -DHAVE_SYS_ACL_H=0 \
"

do_configure () {
	${S}/configure --prefix=${prefix} -- ${CMAKE_EXTRACONF}
}

do_compile() {
	oe_runmake
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}

do_compile[progress] = "percent"
