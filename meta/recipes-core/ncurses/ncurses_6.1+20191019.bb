require ncurses.inc

SRC_URI += "file://0001-tic-hang.patch \
           file://0002-configure-reproducible.patch \
           file://config.cache \
           "
# commit id corresponds to the revision in package version
SRCREV = "ea70ec815b362f5bbad7a827a2edf50fd2b459cc"
S = "${WORKDIR}/git"
EXTRA_OECONF += "--with-abi-version=5 --cache-file=${B}/config.cache"
UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+(\.\d+)+(\+\d+)*)"

CVE_VERSION = "6.1.${@d.getVar("PV").split('+')[1]}"
