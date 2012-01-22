require kexec-tools.inc
export LDFLAGS = "-L${STAGING_LIBDIR}"
EXTRA_OECONF = " --with-zlib=yes"

PR = "r0"

SRC_URI[md5sum] = "1b362abd2e8669171a5ba50a9cc26183"
SRC_URI[sha256sum] = "36a50fad961e24c9cdaa7c01b74d3cdd2b1bebf9bf0f0f3740dd1f21bd1b8ba6"

PACKAGES =+ "kexec kdump"

FILES_kexec = "${sbindir}/kexec"
FILES_kdump = "${sbindir}/kdump"
