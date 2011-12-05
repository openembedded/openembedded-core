# vi:sts=4:sw=4:et
"""Code for parsing OpenEmbedded license strings"""

import ast
import re

class InvalidLicense(StandardError):
    def __init__(self, license):
        self.license = license
        StandardError.__init__(self)

    def __str__(self):
        return "invalid license '%s'" % self.license

license_operator = re.compile('([&|() ])')
license_pattern = re.compile('[a-zA-Z0-9.+_\-]+$')

class LicenseVisitor(ast.NodeVisitor):
    """Syntax tree visitor which can accept OpenEmbedded license strings"""
    def visit_string(self, licensestr):
        new_elements = []
        elements = filter(lambda x: x.strip(), license_operator.split(licensestr))
        for pos, element in enumerate(elements):
            if license_pattern.match(element):
                if pos > 0 and license_pattern.match(elements[pos-1]):
                    new_elements.append('&')
                element = '"' + element + '"'
            elif not license_operator.match(element):
                raise InvalidLicense(element)
            new_elements.append(element)

        self.visit(ast.parse(' '.join(new_elements)))
