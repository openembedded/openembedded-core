require clutter-gst.inc

PV = "0.9.0+git${SRCPV}"
PR = "r2"

SRC_URI = "git://git.clutter-project.org/clutter-gst.git;protocol=git \
           file://autofoo-9db4a61a25677764bb927369c5c68ada958fb65c.patch;patch=1;rev=9db4a61a25677764bb927369c5c68ada958fb65c \
           file://autofoo.patch;patch=1;notrev=9db4a61a25677764bb927369c5c68ada958fb65c"

S = "${WORKDIR}/git"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}
