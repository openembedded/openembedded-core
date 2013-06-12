python multilib_virtclass_handler_global () {
    if not e.data:
        return

    if isinstance(e, bb.event.RecipePreFinalise):
        for v in e.data.getVar("MULTILIB_VARIANTS", True).split():
            if e.data.getVar("TARGET_VENDOR_virtclass-multilib-" + v, False) is None:
                e.data.setVar("TARGET_VENDOR_virtclass-multilib-" + v, e.data.getVar("TARGET_VENDOR", False) + "ml" + v)

    variant = e.data.getVar("BBEXTENDVARIANT", True)

    if isinstance(e, bb.event.RecipeParsed) and not variant:
        if bb.data.inherits_class('kernel', e.data) or \
            bb.data.inherits_class('module-base', e.data) or \
            (bb.data.inherits_class('allarch', e.data) and\
             not bb.data.inherits_class('packagegroup', e.data)):
            variants = (e.data.getVar("MULTILIB_VARIANTS", True) or "").split()

            import oe.classextend
            clsextends = []
            for variant in variants:
                clsextends.append(oe.classextend.ClassExtender(variant, e.data))

            # Process PROVIDES
            origprovs = provs = e.data.getVar("PROVIDES", True) or ""
            for clsextend in clsextends:
                provs = provs + " " + clsextend.map_variable("PROVIDES", setvar=False)
            e.data.setVar("PROVIDES", provs)

            # Process RPROVIDES
            origrprovs = rprovs = e.data.getVar("RPROVIDES", True) or ""
            for clsextend in clsextends:
                rprovs = rprovs + " " + clsextend.map_variable("RPROVIDES", setvar=False)
            e.data.setVar("RPROVIDES", rprovs)

	    # Process RPROVIDES_${PN}...
            for pkg in (e.data.getVar("PACKAGES", True) or "").split():
                origrprovs = rprovs = e.data.getVar("RPROVIDES_%s" % pkg, True) or ""
                for clsextend in clsextends:
                    rprovs = rprovs + " " + clsextend.map_variable("RPROVIDES_%s" % pkg, setvar=False)
                    rprovs = rprovs + " " + clsextend.extname + "-" + pkg
                e.data.setVar("RPROVIDES_%s" % pkg, rprovs)
}

addhandler multilib_virtclass_handler_global
multilib_virtclass_handler_global[eventmask] = "bb.event.RecipePreFinalise bb.event.RecipeParsed"

