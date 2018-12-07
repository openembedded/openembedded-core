#
# Usage:
# - Enable ccache
#   Add the following line to a conffile such as conf/local.conf:
#   INHERIT += "ccache"
#
# - Disable ccache for a recipe
#   Add the following line to the recipe if it can't be built with ccache:
#   CCACHE_DISABLE = '1'
#

export CCACHE_DIR ?= "${TMPDIR}/ccache/${MULTIMACH_TARGET_SYS}/${PN}"

# We need to stop ccache considering the current directory or the
# debug-prefix-map target directory to be significant when calculating
# its hash. Without this the cache would be invalidated every time
# ${PV} or ${PR} change.
export CCACHE_NOHASHDIR ?= "1"

python() {
    """
    Enable ccache for the recipe
    """
    pn = d.getVar('PN')
    # quilt-native doesn't need ccache since no c files
    if not (pn in ('ccache-native', 'quilt-native') or
            bb.utils.to_boolean(d.getVar('CCACHE_DISABLE'))):
        d.appendVar('DEPENDS', ' ccache-native')
        d.setVar('CCACHE', 'ccache ')
}
