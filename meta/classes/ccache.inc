# Make ccache use a TMPDIR specific ccache directory if using the crosscompiler,
# since it isn't likely to be useful with any other toolchain than the one we just
# built, and would otherwise push more useful things out of the default cache.

CCACHE_DIR_TARGET = "${TMPDIR}/ccache"

python () {
	if not bb.data.inherits_class('native', d) and not bb.data.inherits_class('cross', d):
		bb.data.setVar('CCACHE_DIR', '${CCACHE_DIR_TARGET}', d)
		bb.data.setVarFlag('CCACHE_DIR', 'export', '1', d)
}
