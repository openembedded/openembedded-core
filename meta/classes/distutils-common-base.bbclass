EXTRA_OEMAKE = ""

export STAGING_INCDIR
export STAGING_LIBDIR

def python_dir(d):
	import os, bb
	staging_incdir = bb.data.getVar( "STAGING_INCDIR", d, 1 )
	for majmin in "2.6 2.5 2.4 2.3".split():
		if os.path.exists( "%s/python%s" % ( staging_incdir, majmin ) ): return "python%s" % majmin
	if not "python-native" in bb.data.getVar( "DEPENDS", d, 1 ).split():
		raise "No Python in STAGING_INCDIR. Forgot to build python-native ?"
	return "INVALID"

PYTHON_DIR = "${@python_dir(d)}"

PACKAGES = "${PN}-dev ${PN}-dbg ${PN}-doc ${PN}"

FILES_${PN} = "${bindir}/* ${libdir}/* ${libdir}/${PYTHON_DIR}/*"

FILES_${PN}-dev += "\
  ${datadir}/pkgconfig \
  ${libdir}/pkgconfig \
  ${libdir}/${PYTHON_DIR}/site-packages/*.la \
"
FILES_${PN}-dbg = "\
  ${libdir}/${PYTHON_DIR}/site-packages/.debug \
  ${libdir}/${PYTHON_DIR}/site-packages/*/.debug \
  ${libdir}/${PYTHON_DIR}/site-packages/*/*/.debug \
"
