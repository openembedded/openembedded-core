LICENSE = "GPL"
DESCRIPTION = "clipboard manager"
DEPENDS = "virtual/libx11"

SRC_URI = "svn://stage.maemo.org/svn/maemo/projects/haf/tags/clipboard-manager/;module=${PV};proto=https \
	   file://makefile.patch;patch=1  \
	   file://script.patch;patch=1    \
	   file://daemonize.patch;patch=1 \
          "

S = "${WORKDIR}/${PV}"

inherit autotools pkgconfig

do_install_append () {
	mv ${D}/${sysconfdir}/X11/Xsession.d/clipboard.sh ${D}/${sysconfdir}/X11/Xsession.d/70clipboard
	chmod u+x ${D}/${sysconfdir}/X11/Xsession.d/70clipboard
}

pkg_postinst_clipboard-manager () {
#!/bin/sh -e
if [ x"$D" = "x" ]; then
  {
    if [ x$(pidof clipboard-manager) != x ]; then
      kill -TERM $(pidof clipboard-manager)
    fi

    ${sysconfdir}/X11/Xsession.d/70clipboard
  } > /dev/null
else
  exit 1
fi
}
