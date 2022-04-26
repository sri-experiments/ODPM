//
//  PasswordItem+CoreDataProperties.swift
//  ODPM_iOS
//
//  Created by srivats venkataraman on 3/25/22.
//
//

import Foundation
import CoreData


extension PasswordItem {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<PasswordItem> {
        return NSFetchRequest<PasswordItem>(entityName: "PasswordItem")
    }

    @NSManaged public var siteName: String?
    @NSManaged public var createdAt: Date?
    @NSManaged public var password: String?

}

extension PasswordItem : Identifiable {

}
