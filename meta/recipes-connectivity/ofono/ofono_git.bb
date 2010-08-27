require ofono.inc

S	 = "${WORKDIR}/git"
PV	 = "0.12-git${SRCPV}"
PR = "r1"

SRC_URI  = "git://git.kernel.org/pub/scm/network/ofono/ofono.git;protocol=git \
	    file://ofono"

do_configure_prepend () {
  ${S}/bootstrap
}

