require ccache.inc

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://LICENSE.adoc;md5=2722abeaf1750dbf175b9491112374e1"

SRC_URI[md5sum] = "07d4324efbb3c216bbd1c512f4553480"
SRC_URI[sha256sum] = "b2264923c63e2b90a17cf56acb1df3f4229c416fb88e476e5ec7e02919d319c3"

SRC_URI += " \
            file://0002-dev.mk.in-fix-file-name-too-long.patch \
"
