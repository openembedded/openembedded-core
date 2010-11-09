import re

class OEList(list):
    """OpenEmbedded 'list' type

    Acts as an ordinary list, but is constructed from a string value and a
    separator (optional), and re-joins itself when converted to a string with
    str().  Set the variable type flag to 'list' to use this type, and the
    'separator' flag may be specified (defaulting to whitespace)."""

    name = "list"

    def __init__(self, value, separator = None):
        if value is not None:
            list.__init__(self, value.split(separator))
        else:
            list.__init__(self)

        if separator is None:
            self.separator = " "
        else:
            self.separator = separator

    def __str__(self):
        return self.separator.join(self)

def choice(value, choices):
    """OpenEmbedded 'choice' type

    Acts as a multiple choice for the user.  To use this, set the variable
    type flag to 'choice', and set the 'choices' flag to a space separated
    list of valid values."""
    if not isinstance(value, basestring):
        raise TypeError("choice accepts a string, not '%s'" % type(value))

    value = value.lower()
    choices = choices.lower()
    if value not in choices.split():
        raise ValueError("Invalid choice '%s'.  Valid choices: %s" %
                         (value, choices))
    return value

def regex(value, regexflags=None):
    """OpenEmbedded 'regex' type

    Acts as a regular expression, returning the pre-compiled regular
    expression pattern object.  To use this type, set the variable type flag
    to 'regex', and optionally, set the 'regexflags' type to a space separated
    list of the flags to control the regular expression matching (e.g.
    FOO[regexflags] += 'ignorecase').  See the python documentation on the
    're' module for a list of valid flags."""

    flagval = 0
    if regexflags:
        for flag in regexflags.split():
            flag = flag.upper()
            try:
                flagval |= getattr(re, flag)
            except AttributeError:
                raise ValueError("Invalid regex flag '%s'" % flag)

    try:
        return re.compile(value, flagval)
    except re.error, exc:
        raise ValueError("Invalid regex value '%s': %s" %
                         (value, exc.args[0]))

def boolean(value):
    """OpenEmbedded 'boolean' type

    Valid values for true: 'yes', 'y', 'true', 't', '1'
    Valid values for false: 'no', 'n', 'false', 'f', '0'
    """

    if not isinstance(value, basestring):
        raise TypeError("boolean accepts a string, not '%s'" % type(value))

    value = value.lower()
    if value in ('yes', 'y', 'true', 't', '1'):
        return True
    elif value in ('no', 'n', 'false', 'f', '0'):
        return False
    raise ValueError("Invalid boolean value '%s'" % value)

def integer(value, numberbase=10):
    """OpenEmbedded 'integer' type

    Defaults to base 10, but this can be specified using the optional
    'numberbase' flag."""

    return int(value, int(numberbase))

_float = float
def float(value, fromhex='false'):
    """OpenEmbedded floating point type

    To use this type, set the type flag to 'float', and optionally set the
    'fromhex' flag to a true value (obeying the same rules as for the
    'boolean' type) if the value is in base 16 rather than base 10."""

    if boolean(fromhex):
        return _float.fromhex(value)
    else:
        return _float(value)
