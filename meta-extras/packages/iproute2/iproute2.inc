DESCRIPTION = "kernel routing and traffic control utilities"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "flex-native bison-native"

# Set DATE in the .bb file
SRC_URI = "http://developer.osdl.org/dev/iproute2/download/${P}-${DATE}.tar.gz"

S = "${WORKDIR}/${P}-${DATE}"

EXTRA_OEMAKE = "CC='${CC}' KERNEL_INCLUDE=${STAGING_KERNEL_DIR}/include DOCDIR=${docdir}/iproute2 SUBDIRS='lib tc ip' SBINDIR=/sbin"

do_install () {
        oe_runmake DESTDIR=${D} install
}

FILES_${PN} += "/usr/lib/tc/*"
FILES_${PN}-dbg += "/usr/lib/tc/.debug"
