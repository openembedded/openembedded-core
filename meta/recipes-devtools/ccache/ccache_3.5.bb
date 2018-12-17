require ccache.inc

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://LICENSE.adoc;md5=39f34d6ff4df51ed718fd7d243fc4e51"

SRC_URI[md5sum] = "6e62bf2fd2c8fdc749efdca74868da41"
SRC_URI[sha256sum] = "8094f9bc186be4c9db9b480eeac280926bd44039502d1e596f45371b05a85918"

SRC_URI += " \
            file://0002-dev.mk.in-fix-file-name-too-long.patch \
"
