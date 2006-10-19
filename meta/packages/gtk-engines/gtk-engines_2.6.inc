
SRC_URI = "http://ftp.gnome.org/pub/gnome/sources/gtk-engines/2.6/gtk-engines-${PV}.tar.bz2"

S = "${WORKDIR}/gtk-engines-${PV}"

inherit autotools

do_configure_prepend() {
	for i in `ls gtk-common`; do
		ln -sf ../gtk-common/$i gtk2-engine/$i
	done
}

