require gnutls.inc
PR = "r1"

do_configure_prepend() {
	sed -i "s/2.60/2.59/" ${S}/configure.in
}
