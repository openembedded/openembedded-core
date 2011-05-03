def gettext_dependencies(d):
    if d.getVar('USE_NLS', True) == 'no':
        return ""
    if bb.data.getVar('INHIBIT_DEFAULT_DEPS', d, True) and not oe.utils.inherits(d, 'cross-canadian'):
        return ""
    return d.getVar('DEPENDS_GETTEXT', False)

def gettext_oeconf(d):
    # Remove the NLS bits if USE_NLS is no.
    if d.getVar('USE_NLS', True) == 'no':
        return '--disable-nls'
    return "--enable-nls"

DEPENDS_GETTEXT = "virtual/gettext gettext-native"

BASEDEPENDS =+ "${@gettext_dependencies(d)}"
EXTRA_OECONF += "${@gettext_oeconf(d)}"
