def prserv_get_pr_auto(d):
    import oe.prservice
    import re

    pv = d.getVar("PV", True)
    if not d.getVar('PRSERV_HOST', True):
        if 'AUTOINC' in pv:
            d.setVar("PKGV", pv.replace("AUTOINC", "0"))
        bb.warn("Not using network based PR service")
        return None

    version = d.getVar("PRAUTOINX", True)
    pkgarch = d.getVar("PACKAGE_ARCH", True)
    checksum = d.getVar("BB_TASKHASH", True)

    conn = d.getVar("__PRSERV_CONN", True)
    if conn is None:
        conn = oe.prservice.prserv_make_conn(d)
        if conn is None:
            return None

    if "AUTOINC" in pv:
        srcpv = bb.fetch2.get_srcrev(d)
        base_ver = "AUTOINC-%s" % version[:version.find(srcpv)]
        value = conn.getPR(base_ver, pkgarch, srcpv)
        d.setVar("PKGV", pv.replace("AUTOINC", str(value)))

    if d.getVar('PRSERV_LOCKDOWN', True):
        auto_rev = d.getVar('PRAUTO_' + version + '_' + pkgarch, True) or d.getVar('PRAUTO_' + version, True) or None
    else:
        auto_rev = conn.getPR(version, pkgarch, checksum)

    return auto_rev
