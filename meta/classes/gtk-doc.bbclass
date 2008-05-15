# We don't have gtk-doc so disable it
do_configure_prepend() {
	echo "EXTRA_DIST=">> ${S}/gtk-doc.make
}
