SUMMARY = "user and group account administration library"
DESCRIPTION = "The libuser library implements a standardized interface for manipulating and administering user \
and group accounts"
HOMEPAGE = "https://fedorahosted.org/libuser/"
BUGTRACKER = "https://fedorahosted.org/libuser/newticket"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2 \
                    file://lib/user.h;endline=19;md5=76b301f63c39fa992062395efbdc9558 \
                    file://samples/testuser.c;endline=19;md5=3b87fa660fa3f4a6bb31d624afe30ba1"

SECTION = "base"

SRC_URI = "https://fedorahosted.org/releases/l/i/libuser/libuser-${PV}.tar.xz \
           file://fixsepbuild.patch"

SRC_URI[md5sum] = "22835cbfec894b1e9491845ed5023244"
SRC_URI[sha256sum] = "27a93ed1f6424cfbf539c56cf0ca54d3b9b2c4daba6c408464e3c77ddf9c7a2f"
PR = "r0"

DEPENDS = "popt libpam glib-2.0 xz-native docbook-utils-native linuxdoc-tools-native python"

inherit autotools gettext pythonnative python-dir

EXTRA_OEMAKE = "PYTHON_CPPFLAGS=-I${STAGING_INCDIR}/${PYTHON_DIR}"

PACKAGES += "${PN}-python "

FILES_${PN}-dbg += "${PYTHON_SITEPACKAGES_DIR}/.debug"
FILES_${PN}-python = "${PYTHON_SITEPACKAGES_DIR}"

