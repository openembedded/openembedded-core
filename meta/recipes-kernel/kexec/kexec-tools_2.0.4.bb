require kexec-tools.inc
export LDFLAGS = "-L${STAGING_LIBDIR}"
EXTRA_OECONF = " --with-zlib=yes"

SRC_URI[md5sum] = "b9f2a3ba0ba9c78625ee7a50532500d8"
SRC_URI[sha256sum] = "6ba1872c58434b8e92506ff515c7ef64555671af54097bae51b833bda3f5126c"

PACKAGES =+ "kexec kdump"

FILES_kexec = "${sbindir}/kexec"
FILES_kdump = "${sbindir}/kdump"
