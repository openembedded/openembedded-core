python multilib_virtclass_handler () {
    if not isinstance(e, bb.event.RecipePreFinalise):
        return

    cls = e.data.getVar("BBEXTENDCURR", True)
    variant = e.data.getVar("BBEXTENDVARIANT", True)
    if cls != "multilib" or not variant:
        return

    # There should only be one kernel in multilib configs
    if bb.data.inherits_class('kernel', e.data) or bb.data.inherits_class('module-base', e.data):
        raise bb.parse.SkipPackage("We shouldn't have multilib variants for the kernel")

    if bb.data.inherits_class('image', e.data):
        e.data.setVar("PN", variant + "-" + e.data.getVar("PN", False))
        return

    save_var_name=e.data.getVar("MULTILIB_SAVE_VARNAME", True) or ""
    for name in save_var_name.split():
        val=e.data.getVar(name, True)
        if val:
            e.data.setVar(name + "_MULTILIB_ORIGINAL", val)

    # Expand this since this won't work correctly once we set a multilib into place
    e.data.setVar("ALL_MULTILIB_PACKAGE_ARCHS", e.data.getVar("ALL_MULTILIB_PACKAGE_ARCHS", True))
 
    override = ":virtclass-multilib-" + variant

    e.data.setVar("MLPREFIX", variant + "-")
    e.data.setVar("PN", variant + "-" + e.data.getVar("PN", False))
    e.data.setVar("SHLIBSDIR_virtclass-multilib-" + variant ,e.data.getVar("SHLIBSDIR", False) + "/" + variant)
    if e.data.getVar("TARGET_VENDOR_virtclass-multilib-" + variant, False) is None:
	    e.data.setVar("TARGET_VENDOR_virtclass-multilib-" + variant, e.data.getVar("TARGET_VENDOR", False) + "ml" + variant)
    e.data.setVar("OVERRIDES", e.data.getVar("OVERRIDES", False) + override)
}

addhandler multilib_virtclass_handler

STAGINGCC_prepend = "${BBEXTENDVARIANT}-"

python __anonymous () {
    variant = d.getVar("BBEXTENDVARIANT", True)

    import oe.classextend

    clsextend = oe.classextend.ClassExtender(variant, d)

    if bb.data.inherits_class('image', d):
        clsextend.map_depends_variable("PACKAGE_INSTALL")
        clsextend.map_depends_variable("LINGUAS_INSTALL")
        clsextend.map_depends_variable("RDEPENDS")
        pinstall = d.getVar("LINGUAS_INSTALL", True) + " " + d.getVar("PACKAGE_INSTALL", True)
        d.setVar("PACKAGE_INSTALL", pinstall)
        d.setVar("LINGUAS_INSTALL", "")
        # FIXME, we need to map this to something, not delete it!
        d.setVar("PACKAGE_INSTALL_ATTEMPTONLY", "")
        return

    clsextend.rename_packages()
    clsextend.rename_package_variables((d.getVar("PACKAGEVARS", True) or "").split())

    clsextend.map_depends_variable("DEPENDS")
    clsextend.map_packagevars()
    clsextend.map_variable("PROVIDES")
    clsextend.map_variable("PACKAGES_DYNAMIC")
    clsextend.map_variable("PACKAGE_INSTALL")
    clsextend.map_variable("INITSCRIPT_PACKAGES")
}
