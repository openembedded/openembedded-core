def gettext_dependencies(d):
    if d.getVar('USE_NLS', True) == 'no' and not oe.utils.inherits(d, 'native', 'nativesdk', 'cross'):
        return ""
    if d.getVar('INHIBIT_DEFAULT_DEPS', True) and not oe.utils.inherits(d, 'cross-canadian'):
        return ""
    return d.getVar('DEPENDS_GETTEXT', False)

def gettext_oeconf(d):
    # Remove the NLS bits if USE_NLS is no.
    if d.getVar('USE_NLS', True) == 'no':
        return '--disable-nls'
    return "--enable-nls"

DEPENDS_GETTEXT = "virtual/gettext gettext-native"

BASEDEPENDS =+ "${@gettext_dependencies(d)}"
EXTRA_OECONF_append = " ${@gettext_oeconf(d)}"
