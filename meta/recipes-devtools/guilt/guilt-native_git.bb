DESCRIPTION = "guilt is quilt like tool for git"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=b6f3400dc1a01cebafe8a52b3f344135"

inherit native

SRC_URI = "git://repo.or.cz/guilt.git \
	   file://guilt-bash.patch \
          "

SRCREV="c2a5bae511c6d5354aa4e1cb59069c31df2b8eeb"

S="${WORKDIR}/git"

SRC_URI[md5sum] = "d800c5e0743d90543ef51d797a626e09"
SRC_URI[sha256sum] = "64dfe6af1e924030f71163f3aa12cd846c80901d6ff8ef267ea35bb0752b4ba9"

# we don't compile, we just install
do_compile() {
	:
}

do_install() {
	oe_runmake PREFIX=${D}/${prefix} install
}
